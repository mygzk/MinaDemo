package com.gzk.client.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

public class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {
    /**
     * 心跳包内容
     */
    private static final String HEARTBEATREQUEST = MinaConfig.HEARTBEAT_REQUEST;
    private static final String HEARTBEATRESPONSE = MinaConfig.HEARTBEAT_RESPONSE;


    /**
     * 检查是否是请求的心跳包信息
     *
     * @param ioSession ioSession
     * @param o         o
     * @return boolean
     */
    @Override
    public boolean isRequest(IoSession ioSession, Object o) {
        if (o.equals(HEARTBEATREQUEST))
            return true;
        return false;
    }

    /**
     * 这个接口就是用来判断接收到的消息是不是一个心跳回复包。
     *
     * @param ioSession ioSession
     * @param o         o
     * @return boolean
     */
    @Override
    public boolean isResponse(IoSession ioSession, Object o) {
        if (o.equals(HEARTBEATRESPONSE))
            return true;
        return false;
    }

    /**
     * 获取一个回复的心跳包，并且发送出去
     *
     * @param ioSession ioSession
     * @return obj
     */

    @Override
    public Object getRequest(IoSession ioSession) {
        return HEARTBEATREQUEST;
    }

    /**
     * 获取一个心跳回复包。
     *
     * @param ioSession ioSession
     * @param o         o
     * @return boolean
     */

    @Override
    public Object getResponse(IoSession ioSession, Object o) {
        return HEARTBEATRESPONSE;
    }
}
