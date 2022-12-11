package com.dn_alan.eventbus;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dn_alan.eventbus.core.Hermes;
import com.dn_alan.eventbus.manager.IDownManager;
import com.dn_alan.eventbus.manager.IUserManager;
import com.dn_alan.eventbus.manager.UserManager;
import com.dn_alan.eventbus.service.HermesService;


/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class SecondActivity extends Activity {
    IUserManager userManager;
    IDownManager downManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Hermes.getDefault().connect(this, HermesService.class);
    }

//    public void send(View view) {
////        EventBus.getDefault().post(new Friend("alan",18));  //主线程
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                DNEventbus.getDefault().post(new Friend("alan",19)); //子线程
//            }
//        }).start();
//    }

    public void userManager(View view) {
        userManager = Hermes.getDefault().getInstance(IUserManager.class);
        downManager=Hermes.getDefault().getInstance(IDownManager.class);
    }

    public void getUser(View view) {
        //1、接收从服务端传过来的数据
        Toast.makeText(this,"-----> "+userManager.getFriend().toString(), Toast.LENGTH_SHORT).show();
//        userManager.setFriend(new Friend("jett",20));
    }

}

/**
 * 1、
 */
