package com.gzk.client;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gzk.client.mina.ConnectLisenter;
import com.gzk.client.mina.MinaClientManager;
import com.gzk.client.mina.MinaConfig;
import com.gzk.client.mina.MinaReciveFailEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etContent;

    private ListView lsRecord;
    private RecordAdapter mAdapter;
    private List<RecordBean> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        etContent = findViewById(R.id.et_content);
        findViewById(R.id.tv_send).setOnClickListener(this);
        findViewById(R.id.tv_connect).setOnClickListener(this);
        findViewById(R.id.tv_disconnect).setOnClickListener(this);


        lsRecord = findViewById(R.id.ls_record);
        mAdapter = new RecordAdapter(this, mData);
        lsRecord.setAdapter(mAdapter);

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
                        Toast.makeText(MainActivity.this, "server connect fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void connSucc() {
                        Log.e("mina", "server connect succ");
                        Toast.makeText(MainActivity.this, "server connect succ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void connDis() {
                        Log.e("mina", "server connect disconnect");
                        Toast.makeText(MainActivity.this, "server connect disconnect", Toast.LENGTH_SHORT).show();
                    }
                });


        MinaClientManager.getManagerInstance().initConfig(builder.builder());
        MinaClientManager.getManagerInstance().connect();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEvent(RecordBean messageEvent) {
        if (messageEvent != null) {
            mAdapter.addData(messageEvent);
        }else {
            Toast.makeText(MainActivity.this, "receiveEvent is null" , Toast.LENGTH_SHORT).show();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveFailEvent(MinaReciveFailEvent event) {
        Log.e("receive", "receiveFailEvent:" + event.getMsg());
        Toast.makeText(MainActivity.this, "receiveFailEvent:" + event.getMsg(), Toast.LENGTH_SHORT).show();
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
        MinaClientManager.getManagerInstance().send(str);
    }


}
