package com.dn_alan.myapplication.core;

import com.dn_alan.myapplication.manager.DnUserManager;
import com.dn_alan.myapplication.util.TypeUtils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class TypeCenter {

    //为了减少反射，所以保存起来
    private final ConcurrentHashMap<Class<?>,ConcurrentHashMap<String, Method>> mRawMethods;
    private final ConcurrentHashMap<String,Class<?>> mClazz;

    private static final TypeCenter ourInstance = new TypeCenter();

    public TypeCenter() {
        mRawMethods = new ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Method>>();
        mClazz = new ConcurrentHashMap<>();
    }

    public static TypeCenter getInstance(){
        return ourInstance;
    }

    public void register(Class<DnUserManager> clazz) {
        //注册--》类， 注册--》方法
        registerClass(clazz);
        registerMethod(clazz);
    }

    //缓存class
    private void registerClass(Class<DnUserManager> clazz) {
        String name =  clazz.getName();
        mClazz.putIfAbsent(name, clazz);
    }

    private void registerMethod(Class<DnUserManager> clazz){
        Method[] methods = clazz.getMethods();
        for(Method method: methods){
            mRawMethods.putIfAbsent(clazz, new ConcurrentHashMap<String, Method>());
            ConcurrentHashMap<String, Method> map = mRawMethods.get(clazz);
            String methodId = TypeUtils.getMethodId(method);
            map.put(methodId, method);
        }
    }
}
