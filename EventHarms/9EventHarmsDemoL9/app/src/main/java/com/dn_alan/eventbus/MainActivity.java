package com.dn_alan.eventbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dn_alan.eventbus.core.DNEventbus;
import com.dn_alan.eventbus.core.DNSubscribe;
import com.dn_alan.eventbus.core.DNThreadMode;
import com.dn_alan.eventbus.core.Hermes;
import com.dn_alan.eventbus.manager.DownManager;
import com.dn_alan.eventbus.manager.UserManager;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        EventBus.getDefault().register(this);
//        textView = (TextView) findViewById(R.id.tv);

        //为什么要初始化（在进程中，单例存在不同，所以需要初始化）
        Hermes.getDefault().init(this);

        Hermes.getDefault().register(UserManager.class);
        UserManager.getInstance().setFriend(new Friend("alan", 18));
//        Hermes.getDefault().register(DownManager.class);
        DownManager.getInstance().setFileRecord(new FileRecord("/sdcard/0",12344));
    }

    public void change(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

//    @DNSubscribe(threadMode = DNThreadMode.MAIN)
//    public void receive(Friend friend) {
//        textView.setText(friend.toString() +   "===" +Thread.currentThread().getName());
//        Toast.makeText(this, friend.toString(), Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        DNEventbus.getDefault().unregister(this);
    }

    public void getPerson(View view) {

        Toast.makeText(this, UserManager.getInstance().getFriend().toString(), Toast.LENGTH_SHORT).show();
    }
}
