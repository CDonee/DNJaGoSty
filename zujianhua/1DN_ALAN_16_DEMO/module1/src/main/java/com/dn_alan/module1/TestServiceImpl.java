package com.dn_alan.module1;

import android.util.Log;

import com.dn_alan.base.TestService;
import com.dn_alan.router_annotation.Route;

@Route(path = "/module1/service")
public class TestServiceImpl implements TestService {

    @Override
    public void test() {
        Log.i("Service", "我是Module1模块测试服务通信");
    }
}
