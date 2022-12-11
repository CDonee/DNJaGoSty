package com.dn_alan.myapplication;

import android.util.Log;

import com.dn_alan.base.TestService;
import com.dn_alan.router_annotation.Route;


@Route(path = "/main/service2")
public class TestServiceImpl2 implements TestService {


    @Override
    public void test() {
        Log.i("Service", "我是app模块测试服务通信2");
    }
}
