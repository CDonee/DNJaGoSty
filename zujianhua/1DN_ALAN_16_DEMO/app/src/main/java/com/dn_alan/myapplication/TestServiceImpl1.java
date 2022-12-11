package com.dn_alan.myapplication;

import android.util.Log;

import com.dn_alan.base.TestService;
import com.dn_alan.router_annotation.Route;


@Route(path = "/main/service1")
public class TestServiceImpl1 implements TestService {


    @Override
    public void test() {
        Log.i("Service", "我是app模块测试服务通信1");
    }
}
