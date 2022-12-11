package com.dn_alan.myapplication.core;

import android.content.Context;

import com.dn_alan.myapplication.SecondActivity;
import com.dn_alan.myapplication.manager.DnUserManager;
import com.dn_alan.myapplication.service.HermesService;

import java.util.logging.Handler;

public class Hermes {
    private Context mContext;
    private TypeCenter typeCenter;
    private ServiceConnectionManager serviceConnectionManager;

    private static final Hermes ourInstance = new Hermes();

    public Hermes() {
        serviceConnectionManager = ServiceConnectionManager.getInstance();
        typeCenter = TypeCenter.getInstance();
    }

    public static Hermes getDefault(){
        return ourInstance;
    }

    public void init(Context context){
        this.mContext =  context.getApplicationContext();
    }

    //----------------------------服务端-------------------------
    public void register(Class<DnUserManager> clazz) {
        typeCenter.register(clazz);
    }




    //----------------------------客户端-------------------------
    public void connect(Context context,
                        Class<HermesService> hermesServiceClass) {
        connectApp(context, null, hermesServiceClass);
    }

    private void connectApp(Context context, String packageName, Class<HermesService> hermesServiceClass) {
        init(context);
        serviceConnectionManager.bind(context.getApplicationContext(), packageName, hermesServiceClass);
    }
}
