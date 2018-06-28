package com.gzk.client.mina;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.greenrobot.eventbus.EventBus;

import java.net.InetSocketAddress;

public class MinaClientManager {
    private MinaConfig mConfig;
    private NioSocketConnector mConnection;
    private IoSession mSession;
    private InetSocketAddress mAddress;
    private volatile boolean mInit = false;
    private volatile boolean mIsConnect = false;
    private Thread mThread;


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
    public void initConfig(MinaConfig config) {
        if (config == null) {
            mInit = false;
            return;
        }
        this.mConfig = config;
        mInit = true;
    }

    private void init() {
        if (!mInit) {
            new Throwable("MinaClientManager is not initConfig...");
        }
        mConnection = new NioSocketConnector();
        //设置读数据大小
        mConnection.getSessionConfig().setReadBufferSize(mConfig.getReadBufferSize());
        //添加日志过滤
        mConnection.getFilterChain().addLast("Logging", new LoggingFilter());
        //编码过滤
        mConnection.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory()));
       /* mConnection.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"),
                        LineDelimiter.WINDOWS.getValue(), LineDelimiter.WINDOWS.getValue())));
*/
        mConnection.getFilterChain().addLast("heartbeat", getKeep());
        //设置连接远程服务器的IP地址和端口
        mAddress = new InetSocketAddress(mConfig.getIp(), mConfig.getPort());
        mConnection.setDefaultRemoteAddress(mAddress);

        //事物处理
        mConnection.setHandler(new MinaClientHandler());
        //设置监听函数
        mConnection.addListener(new ConnectStatusChange(mConfig.getConnectLisenter()));
    }

    private KeepAliveFilter getKeep() {
        KeepAliveMessageFactory heartBeatFactory = new KeepAliveMessageFactoryImpl();
        KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory,
                IdleStatus.BOTH_IDLE, new KeepAliveRequestTimeoutHandlerImpl());
        //设置是否forward到下一个filter 回复
        heartBeat.setForwardEvent(true);
        //设置心跳频率
        heartBeat.setRequestInterval(4);
        return heartBeat;
    }

    /**
     * 连接方法（外部调用）
     */
    public void connect() {
        if (!mInit) {
            new Throwable("MinaClientManager is not initConfig...");
        }
        if (mIsConnect) {
            return;
        }
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                mIsConnect = connServer();
            }
        });

        mThread.start();
    }


    private boolean connServer() {
        try {
            init();
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
        if (mIsConnect && mSession != null && mSession.isConnected()) {
            mSession.write(msg);
        } else {
            EventBus.getDefault().post(new MinaReciveFailEvent("send msg fail。server may be not connected... "));
        }
    }

    /**
     * 断开连接方法（外部调用）
     */
    public void disConnect() {
        mIsConnect = false;
        try {
            if (mThread != null) {
                mThread.interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mThread = null;
        //关闭
        if (mConnection != null) {
            mConnection.dispose();
        }
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
        mIsConnect = false;
        //关闭
        mConnection.dispose();
        mIsConnect = connServer();
    }


}
