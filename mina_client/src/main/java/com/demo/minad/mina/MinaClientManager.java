package com.demo.minad.mina;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MinaClientManager {
    private MinaConfig mConfig;
    private NioSocketConnector mConnection;
    private IoSession mSession;
    private InetSocketAddress mAddress;
    private boolean mInit = false;
    private Thread mThread;

    private ConnectLisenter mConnectLisenter;

    private static final class ConnectionManagerFactory {
        private static final MinaClientManager mConnectionManager = new MinaClientManager();
    }

    /**
     * 获取ConnectionManager实例 ,静态内部类模式
     */
    public static MinaClientManager getManagerInstance() {
        return ConnectionManagerFactory.mConnectionManager;
    }

    private MinaClientManager() {

    }


    //通过构建者模式来进行初始化
    public void init(MinaConfig config) {
        if (config == null) {
            mInit = false;
            return;
        }
        this.mConfig = config;
        mConnection = new NioSocketConnector();
        //设置读数据大小
        mConnection.getSessionConfig().setReadBufferSize(mConfig.getReadBufferSize());
        //添加日志过滤
        mConnection.getFilterChain().addLast("Logging", new LoggingFilter());
        //编码过滤
        mConnection.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory()));
      /*  mConnection.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"),
                        LineDelimiter.WINDOWS.getValue(), LineDelimiter.WINDOWS.getValue())));
*/
        //设置连接远程服务器的IP地址和端口
        mAddress = new InetSocketAddress(mConfig.getIp(), mConfig.getPort());
        mConnection.setDefaultRemoteAddress(mAddress);

        //事物处理
        mConnection.setHandler(new MinaClientHandler());
        mInit = true;
    }


    /**
     * 连接方法（外部调用）
     */
    public void connect() {
        if (!mInit) {
            new Throwable("MinaClientManager is not init...");
        }
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                connServer();
            }
        });

        mThread.start();
    }


    private boolean connServer() {
        try {
            mConnection.addListener(new ConnectStatusChange(mConfig.getConnectLisenter()));
            ConnectFuture futrue = mConnection.connect();
            futrue.awaitUninterruptibly();
            mSession = futrue.getSession();
        } catch (Exception e) {
            mConfig.getConnectLisenter().connFail();
            e.printStackTrace();
            return false;
        }

        return mSession == null ? false : true;
    }

    /**
     * 发送消息
     *
     * @param msg msg
     */
    public synchronized void send(String msg) {
        if (mSession != null && mSession.isConnected()) {
            mSession.write(msg);
        }
    }

    /**
     * 断开连接方法（外部调用）
     */
    public void disConnect() {
        try {
            mThread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //关闭
        mConnection.dispose();
        //大对象置空
        mConnection = null;
        mSession = null;
        mAddress = null;
        mInit = false;
    }

    /**
     * 重新连接
     */
    public void reConnect() {
        //关闭
        mConnection.dispose();
        connServer();
    }


}
