package com.dn_alan.skin_core2;

import android.app.Activity;
import android.app.Application;

public class SkinManager {
    private static SkinManager instance;
    private Application application;

    private SkinManager(Application application) {
        this.application = application;


        /**
         * 提供了一个应用生命周期回调的注册方法，
         *          * 用来对应用的生命周期进行集中管理，
         *  这个接口叫registerActivityLifecycleCallbacks，可以通过它注册
         *          * 自己的ActivityLifeCycleCallback，每一个Activity的生命周期都会回调到这里的对应方法。
         */
        application.registerActivityLifecycleCallbacks(new SkinActivityLifecycle());
    }

    public static void init(Application application){
        synchronized (SkinManager.class) {
            if(null == instance){
                instance = new SkinManager(application);
            }
        }
    }
}
