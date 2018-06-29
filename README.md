# MinaDemo
mina android client  server

<img src="img/device-2018-06-29-162040.png"  width="30%" height="30%" align="center" />

## 连接参数设置
```
   MinaConfig.Builder builder = new MinaConfig.Builder()
                .setIp(Constant.MINA_HOST)
                .setConnectionTimeout(10000)
                .setReadBuilder(10240)
                .setPort(Constant.MINA_PORT)
                .setConnectLisenter(new ConnectLisenter() {
                    @Override
                    public void connFail() {
                        Log.e("mina", "server connect fail");
                        //Toast.makeText(MainActivity.this, "server connect fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void connSucc() {
                        Log.e("mina", "server connect succ");
                        //Toast.makeText(MainActivity.this, "server connect succ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void connDis() {
                        Log.e("mina", "server connect disconnect");
                        //Toast.makeText(MainActivity.this, "server connect disconnect", Toast.LENGTH_SHORT).show();
                    }
                });


        MinaClientManager.getManagerInstance().initConfig(builder.builder());
        MinaClientManager.getManagerInstance().connect();

```
## 接收发送消息
发送消息
```
 public synchronized void send(String msg) {
        if (mIsConnect && mSession != null && mSession.isConnected()) {
            mSession.write(msg);
        } else {
            EventBus.getDefault().post(new MinaReciveFailEvent("send msg fail。server may be not connected... "));
        }
    }
```
接收消息在
```
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String str = (String)message;
        Log.e(TAG, "前台收到消息如下：" + message);
        RecordBean recordBean= GsonUtil.fromJson(str, RecordBean.class);
        EventBus.getDefault().post(recordBean);
    }

```

## 心跳设置
```
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

     ...
     ...
 mConnection.getFilterChain().addLast("heartbeat", getKeep());

```
## 编码设置
demo中用到到TextLineCodecFactory  解码器编码器 这个是以换行符号区分一条消息 可设置消息大小
```
TextLineCodecFactory textLineCodecFactory=   new TextLineCodecFactory(Charset.forName("UTF-8"));
        textLineCodecFactory.setDecoderMaxLineLength(1024*1024);
        textLineCodecFactory.setEncoderMaxLineLength(1024*1024);
        mConnection.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(textLineCodecFactory));
```



