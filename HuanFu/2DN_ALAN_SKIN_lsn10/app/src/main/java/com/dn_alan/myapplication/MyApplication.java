package com.dn_alan.myapplication;

import android.app.Application;

import com.dn_alan.skin_core2.SkinManager;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
