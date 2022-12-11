package com.dn_alan.myapplication;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.dn_alan.skin_core2.SkinManager;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        SkinManager.init(this);

        //根据app上次退出的状态来判断是否需要设置夜间模式,提前在SharedPreference中存了一个是
        // 否是夜间模式的boolean值
        boolean isNightMode = NightModeConfig.getInstance().getNightMode(getApplicationContext());
        if (false) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
