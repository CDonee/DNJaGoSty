package com.dn_alan.eventbus.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.dn_alan.eventbus.MyEventBusService;
import com.dn_alan.eventbus.Request;
import com.dn_alan.eventbus.Responce;
import com.dn_alan.eventbus.core.Hermes;
import com.dn_alan.eventbus.responce.InstanceResponceMake;
import com.dn_alan.eventbus.responce.ObjectResponceMake;
import com.dn_alan.eventbus.responce.ResponceMake;

/**
 * Created by Administrator on 2018/5/21.
 */

public class HermesService  extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    private MyEventBusService.Stub mBinder=new MyEventBusService.Stub() {
        @Override
        public Responce send(Request request) throws RemoteException {
//            对请求参数进行处理  生成Responce结果返回
            ResponceMake responceMake = null;
            switch (request.getType()) {   //根据不同的类型，产生不同的策略
                case Hermes.TYPE_GET://获取单例
                    responceMake = new InstanceResponceMake();
                    break;
                case Hermes.TYPE_NEW:
                    responceMake = new ObjectResponceMake();
                    break;
            }


            return responceMake.makeResponce(request);
        }
    };
}
