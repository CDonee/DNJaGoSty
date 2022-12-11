package com.dn_alan.eventbus.core;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/5/18.
 */
//注册类中的注册方法
public class SubscribleMethod {
    //注册方法
    private Method method;
    //线程类型
    private DNThreadMode threadMode;
    /**
     * 参数类型
     */
    private Class<?>  eventType;

    public SubscribleMethod(Method method, DNThreadMode threadMode, Class<?> eventType) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
    }

    public Method getMethod() {
        return method;
    }

    public DNThreadMode getThreadMode() {
        return threadMode;
    }

    public Class<?> getEventType() {
        return eventType;
    }
}
