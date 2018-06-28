package com.demo.minad.mina;

public class MinaReciveFailEvent {
    private String msg;

    public MinaReciveFailEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
