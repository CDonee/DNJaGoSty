package com.dn_alan.module2;

import android.app.Application;

import com.dn_alan.router_core.DNRouter;


/**
 * Created by Administrator on 2018/3/21.
 */

public class Module2Application extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DNRouter.init(this);
    }
}
