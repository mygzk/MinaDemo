package com.gzk.client.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;

public class KeepAliveRequestTimeoutHandlerImpl implements KeepAliveRequestTimeoutHandler {
    private static final String HEARTBEATREQUEST = MinaConfig.HEARTBEAT_REQUEST;

    @Override
    public void keepAliveRequestTimedOut(KeepAliveFilter keepAliveFilter, IoSession ioSession) throws Exception {
        if (ioSession != null && ioSession.isActive()) {
            ioSession.write(HEARTBEATREQUEST);
        } else {
            MinaClientManager.getManagerInstance().reConnect();
        }

    }
}
