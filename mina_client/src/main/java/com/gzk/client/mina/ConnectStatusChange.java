package com.gzk.client.mina;

import android.util.Log;

import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * 连接状态监听
 */
public class ConnectStatusChange implements IoServiceListener {
    private String TAG = ConnectStatusChange.class.getSimpleName();

    private ConnectLisenter mConnectLisenter;

    public ConnectStatusChange(ConnectLisenter mConnectLisenter) {
        this.mConnectLisenter = mConnectLisenter;
    }

    @Override
    public void serviceActivated(IoService ioService) throws Exception {
        Log.e(TAG, "==serviceActivated==");

    }

    @Override
    public void serviceIdle(IoService ioService, IdleStatus idleStatus) throws Exception {
        Log.e(TAG, "==serviceIdle==");
    }

    @Override
    public void serviceDeactivated(IoService ioService) throws Exception {
        Log.e(TAG, "==serviceDeactivated==");
    }

    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        Log.e(TAG, "==sessionCreated==");
        if(mConnectLisenter!=null){
            mConnectLisenter.connSucc();
        }
    }

    @Override
    public void sessionClosed(IoSession ioSession) throws Exception {
        Log.e(TAG, "==sessionClosed==");
        if(mConnectLisenter!=null){
            mConnectLisenter.connDis();
        }
    }

    @Override
    public void sessionDestroyed(IoSession ioSession) throws Exception {
        Log.e(TAG, "==sessionDestroyed==");
        if(mConnectLisenter!=null){
            mConnectLisenter.connDis();
        }
    }
}
