package com.dn_alan.skin_core2;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.dn_alan.skin_core2.utils.SkinPreference;
import com.dn_alan.skin_core2.utils.SkinResources;

import java.lang.reflect.Method;
import java.util.Observable;

public class SkinManager extends Observable {
    private static SkinManager instance;
    private Application application;

    public static void init(Application application){
        synchronized (SkinManager.class) {
            if(null == instance){
                instance = new SkinManager(application);
            }
        }
    }

    public static SkinManager getInstance() {
        return instance;
    }

    private SkinManager(Application application) {
        this.application = application;
        //共享首选项 用于记录当前使用的皮肤
        SkinPreference.init(application);
        //资源管理类 用于从app/皮肤 中加载资源
        SkinResources.init(application);
        /**
         * 提供了一个应用生命周期回调的注册方法，
         *          * 用来对应用的生命周期进行集中管理，
         *  这个接口叫registerActivityLifecycleCallbacks，可以通过它注册
         *          * 自己的ActivityLifeCycleCallback，每一个Activity的生命周期都会回调到这里的对应方法。
         */
         application.registerActivityLifecycleCallbacks(new SkinActivityLifecycle());


         loadSkin(SkinPreference.getInstance().getSkin());
    }

    public void loadSkin(String path) {
        if(TextUtils.isEmpty(path)){
            // 记录使用默认皮肤
            SkinPreference.getInstance().setSkin("");
            //清空资源管理器， 皮肤资源属性等
            SkinResources.getInstance().reset();
        } else {
            try {
                //反射创建AssetManager
                AssetManager manager = AssetManager.class.newInstance();
                // 资料路径设置 目录或者压缩包
                Method addAssetPath = manager.getClass().getMethod("addAssetPath", String.class);
                addAssetPath.invoke(manager, path);

                Resources appResources = this.application.getResources();
                Resources skinResources = new Resources(manager, 
                        appResources.getDisplayMetrics(), appResources.getConfiguration());

                //记录
                SkinPreference.getInstance().setSkin(path);
                //获取外部Apk（皮肤薄） 包名
                PackageManager packageManager = this.application.getPackageManager();
                PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
                String packageName = packageArchiveInfo.packageName;

                SkinResources.getInstance().applySkin(skinResources,packageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //采集的view   皮肤包
        setChanged();
        //通知观者者
        notifyObservers();
    }
}
