package com.dn_alan.eventbus.core;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/5/18.
 */

public class DNEventbus {

    private static DNEventbus instance = new DNEventbus();

    //线程池
    private ExecutorService executorService;
    //总表
    private Map<Object, List<SubscribleMethod>> cacheMap;

    private Handler handler;

    public static DNEventbus getDefault() {
        return instance;
    }

    private DNEventbus() {
        this.cacheMap = new HashMap<>();
        executorService = Executors.newCachedThreadPool();
        handler = new Handler(Looper.getMainLooper());
    }

    //注册
    public void register(Object subscriber) {
        Class<?> clazz = subscriber.getClass();
        List<SubscribleMethod> list = cacheMap.get(subscriber);
//        如果已经注册  就不需要注册
        if (list == null) {
            list = getSubscribleMethods(subscriber);
            cacheMap.put(subscriber, list);
        }
    }

    // 取消注册
    public void unregister(Object subscriber) {
        Class<?> clazz = subscriber.getClass();
        List<SubscribleMethod> list = cacheMap.get(subscriber);
        //如果已经注册,就取消注册
        if (list != null) {
            cacheMap.remove(subscriber);
        }
    }

    //寻找能够接受事件的方法
    private List<SubscribleMethod> getSubscribleMethods(Object obj) {
        List<SubscribleMethod> list = new ArrayList<>();

        Class clazz = obj.getClass();

//需要  Object  BaseActivity   ---->Activity(找 )
        while (clazz != null) {

            //判断父类是在那个包下（如果时系统的就不需要）
            String name = clazz.getName();
            if (name.startsWith("java.") ||
                    name.startsWith("javax.") ||
                    name.startsWith("android.") ||
                    name.startsWith("androidx.")) {
                break;
            }
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                DNSubscribe subscribe = method.getAnnotation(DNSubscribe.class);
                if (subscribe == null) {
                    continue;
                }
//                监测这个方法 合不合格
                Class[] paratems = method.getParameterTypes();
                if (paratems.length != 1) {
                    throw new RuntimeException("eventbus只能接收到一个参数");
                }
//                符合要求
                DNThreadMode threadMode = subscribe.threadMode();
                SubscribleMethod subscribleMethod = new SubscribleMethod(method
                        , threadMode, paratems[0]);
                list.add(subscribleMethod);
            }
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    //通知
    public void post(final Object friend) {

        //遍历查找注册类
        Set<Object> set = cacheMap.keySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            //拿到注册类
            final Object activity = iterator.next();
            //获取类中所有添加注解的方法
            List<SubscribleMethod> list = cacheMap.get(activity);
            for (final SubscribleMethod subscribleMethod : list) {
                // 判断 这个方法是否应该接受事件
                if (subscribleMethod.getEventType().isAssignableFrom(friend.getClass())) {
                    switch (subscribleMethod.getThreadMode()) {
                        // 接受方法在子线程执行的情况
                        case ASYNC:
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                // post方法  执行在主线程
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, activity, friend);
                                    }
                                });
                            } else {
                                // post方法  执行在子线程
                                invoke(subscribleMethod, activity, friend);
                            }
                            break;
                        //接受方法在主线程执行的情况
                        case MAIN:
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                invoke(subscribleMethod, activity, friend);
                            } else {
                                // post方法  执行在子线程     接受消息 在主线程
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, activity, friend);
                                    }
                                });
                            }
                            break;
                        case POSTING:
                    }
                }
            }
        }
    }

    //通过反射调用
    private void invoke(SubscribleMethod subscribleMethod, Object activity, Object friend) {
        Method method = subscribleMethod.getMethod();
        try {
            method.invoke(activity, friend);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
