package com.demo.minad.mina;

import android.util.Log;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.greenrobot.eventbus.EventBus;


public class MinaClientHandler extends IoHandlerAdapter {
    private static String TAG = MinaClientHandler.class.getSimpleName();

    public MinaClientHandler() {
    }


    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String str = (String)message;
        EventBus.getDefault().post(new MinaReciveEvent(str));
        Log.e(TAG, "前台收到消息如下：" + message);

    }

    @Override
    public void messageSent(IoSession arg0, Object arg1) throws Exception {
        Log.e(TAG, arg0.getId() + " messageSent");

    }

    @Override
    public void sessionClosed(IoSession session) {
        Log.e(TAG, session.getId() + " sessionClosed");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        Log.e(TAG, session.getId() + " sessionOpened");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
        EventBus.getDefault().post(new MinaReciveFailEvent(cause.getMessage()));
        cause.printStackTrace();
        /*if(session.isActive()){
            session.closeNow();
        }*/

    }
}