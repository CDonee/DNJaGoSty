package com.dn_alan.skin_core2;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.dn_alan.skin_core2.utils.SkinThemeUtils;

import java.lang.reflect.Field;
import java.util.HashMap;

public class SkinActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    HashMap<Activity , SkinLayoutFactory> factoryHashMap = new HashMap<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        /**
         *  更新状态栏
         */
        SkinThemeUtils.updataStatusBarColor(activity);

        /**
         * 更新字体
         */
        Typeface skinTypeface = SkinThemeUtils.getSkinTypeface(activity);

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        try {
            Field mFactorySet = LayoutInflater.class.getDeclaredField("mFactorySet");
            mFactorySet.setAccessible(true);
            mFactorySet.setBoolean(layoutInflater, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //添加自定义创建View 工厂
        SkinLayoutFactory factory = new SkinLayoutFactory(activity,skinTypeface);
        layoutInflater.setFactory2(factory);

        //注册观察者
        SkinManager.getInstance().addObserver(factory);
        factoryHashMap.put(activity, factory);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //删除观察者
        SkinLayoutFactory remove = factoryHashMap.remove(activity);
        SkinManager.getInstance().deleteObserver(remove);
    }
}
