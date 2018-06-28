package com.gzk.server;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

public class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {

    private String TAG = KeepAliveMessageFactoryImpl.class.getSimpleName();
    /**
     * 心跳包内容
     */
    private static final String HEARTBEATREQUEST = "0x11";
    private static final String HEARTBEATRESPONSE = "0x12";


    @Override
    public boolean isRequest(IoSession ioSession, Object o) {
        System.out.println(TAG+" isRequest:"+ (String) o);
        if (o.equals(HEARTBEATREQUEST))
            return true;
        return false;
    }

    @Override
    public boolean isResponse(IoSession ioSession, Object o) {
        System.out.println(TAG+" isResponse:"+ (String) o);
        if (o.equals(HEARTBEATRESPONSE))
            return true;
        return false;
    }

    @Override
    public Object getRequest(IoSession ioSession) {
        System.out.println(TAG+" getRequest:");
        return HEARTBEATREQUEST;
    }

    @Override
    public Object getResponse(IoSession ioSession, Object o) {
        System.out.println(TAG+" getResponse:");
        return HEARTBEATRESPONSE;
    }
}
