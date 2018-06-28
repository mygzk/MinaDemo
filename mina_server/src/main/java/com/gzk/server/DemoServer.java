package com.gzk.server;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class DemoServer {
    // 端口号，要求客户端与服务器端一致
    private static int PORT = 60008;

    public static void main(String[] args) {
        IoAcceptor acceptor = null;
        try {
            // 创建一个非阻塞的server端的Socket
            acceptor = new NioSocketAcceptor();
            // 设置过滤器（使用mina提供的文本换行符编解码器）
            acceptor.getFilterChain().addLast("codec",
                    new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"),
                            LineDelimiter.WINDOWS.getValue(), LineDelimiter.WINDOWS.getValue())));
            // 自定义的编解码器
           /* acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                    new ObjectSerializationCodecFactory()));*/
            // 设置读取数据的换从区大小
            acceptor.getSessionConfig().setReadBufferSize(2048);
            // 读写通道10秒内无操作进入空闲状态
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
            // 为接收器设置管理服务
            acceptor.setHandler(new DemoServerHandler());

            acceptor.getFilterChain().addLast("heartbeat", getKeep());

            // 绑定端口
            acceptor.bind(new InetSocketAddress(PORT));
            System.out.println("服务器启动成功... 端口号未：" + PORT);

        } catch (Exception e) {
            System.out.println("服务器启动异常...");
            e.printStackTrace();
        }

    }

    private static KeepAliveFilter getKeep() {
        KeepAliveMessageFactory heartBeatFactory = new KeepAliveMessageFactoryImpl();
        KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory,
                IdleStatus.BOTH_IDLE);
        //设置是否forward到下一个filter  回发
        heartBeat.setForwardEvent(true);
        //设置心跳频率
        heartBeat.setRequestInterval(5);
        return heartBeat;
    }
}
