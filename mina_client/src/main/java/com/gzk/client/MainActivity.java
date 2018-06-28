package com.gzk.client;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.gzk.client.mina.ConnectLisenter;
import com.gzk.client.mina.MinaClientManager;
import com.gzk.client.mina.MinaConfig;
import com.gzk.client.mina.MinaReciveEvent;
import com.gzk.client.mina.MinaReciveFailEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        etContent = findViewById(R.id.et_content);
        findViewById(R.id.tv_send).setOnClickListener(this);
        findViewById(R.id.tv_connect).setOnClickListener(this);
        findViewById(R.id.tv_disconnect).setOnClickListener(this);
        initMina();

    }

    private void initMina() {
        MinaConfig.Builder builder = new MinaConfig.Builder()
                .setIp(Constant.MINA_HOST)
                .setConnectionTimeout(10000)
                .setReadBuilder(10240)
                .setPort(Constant.MINA_PORT)
                .setConnectLisenter(new ConnectLisenter() {
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
                });


        MinaClientManager.getManagerInstance().initConfig(builder.builder());
        MinaClientManager.getManagerInstance().connect();
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MinaClientManager.getManagerInstance().send(getTestData());
            }
        }, 2 * 1000);
*/
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEvent(MinaReciveEvent messageEvent) {
        Log.e("receive", "messageEvent:" +  messageEvent.getStr());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveFailEvent(MinaReciveFailEvent event) {
        Log.e("receive", "receiveFailEvent:" + event.getMsg());
    }

    private String getTestData() {
        if (true) {
            return "{\"token\":\"\",\"uId\":\"Tmp-6N5erbXezjXxsvOA9Y7piavRehAlTMSh\",\"chatId\":\"GUiroWAHZxgVJcZvSMicgB8n\",\"message\":\"13466695021\",\"messageType\":5002,\"scene\":0,\"objects\":null,\"timeCreate\":1530093284243}";
        }
        Chat chat = new Chat();
        chat.setToken("testasfdasdafs");
        chat.setuId("test");
        chat.setChatId("0");
        chat.setMessage("test");
        chat.setMessageType(0);


        return GsonUtil.toJson(chat);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send:
                send();
                break;
            case R.id.tv_connect:
                connect();
                break;
            case R.id.tv_disconnect:
                MinaClientManager.getManagerInstance().disConnect();
                break;
            default:
                break;
        }
    }

    private void connect() {
        MinaClientManager.getManagerInstance().connect();

    }

    private void send() {
        final String str = etContent.getText().toString();
        Chat chat = GsonUtil.fromJson(getTestData(), Chat.class);
        chat.message = str;
        MinaClientManager.getManagerInstance().send(GsonUtil.toJson(chat));
    }


}
