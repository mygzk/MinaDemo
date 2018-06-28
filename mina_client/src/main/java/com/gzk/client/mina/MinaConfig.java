package com.gzk.client.mina;

import android.util.Log;

public class MinaConfig {
    /**
     * 心跳包内容
     */
    public static final String HEARTBEAT_REQUEST = "0x11";
    public static final String HEARTBEAT_RESPONSE = "0x12";


    private String ip;

    private int port;

    private int readBufferSize = 10 * 1024;

    private long connectionTime = 5 * 1000L;

    private ConnectLisenter connectLisenter;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getReadBufferSize() {
        return readBufferSize;
    }

    public void setReadBufferSize(int readBufferSize) {
        this.readBufferSize = readBufferSize;
    }

    public long getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(long connectionTime) {
        this.connectionTime = connectionTime;
    }

    public ConnectLisenter getConnectLisenter() {
        if (connectLisenter == null) {
            return mDefaultConnectLisenter;
        }
        return connectLisenter;
    }

    public void setConnectLisenter(ConnectLisenter connectLisenter) {
        this.connectLisenter = connectLisenter;
    }

    public static class Builder {
        private String ip;

        private int port;

        private int readBufferSize;

        private long connectionTimeout;

        private ConnectLisenter connectLisenter;

        public Builder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setReadBuilder(int size) {
            this.readBufferSize = size;
            return this;
        }

        public Builder setConnectionTimeout(int time) {
            this.connectionTimeout = time;
            return this;
        }

        public Builder setConnectLisenter(ConnectLisenter connectLisenter) {
            this.connectLisenter = connectLisenter;
            return this;
        }

        public MinaConfig builder() {

            MinaConfig config = new MinaConfig();
            config.ip = this.ip;
            config.port = this.port;
            config.readBufferSize = this.readBufferSize;
            config.connectionTime = this.connectionTimeout;
            config.connectLisenter = this.connectLisenter;
            return config;
        }
    }

    private ConnectLisenter mDefaultConnectLisenter = new ConnectLisenter() {
        @Override
        public void connFail() {
            Log.e("mina", "server connect fail");
        }

        @Override
        public void connSucc() {
            Log.e("mina", "server connect succ");
        }

        @Override
        public void connDis() {
            Log.e("mina", "server connect disconnect");
        }
    };
}
