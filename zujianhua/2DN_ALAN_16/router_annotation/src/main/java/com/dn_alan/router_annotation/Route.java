package com.dn_alan.router_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//元注解
@Target(ElementType.TYPE)   //表示注解的作用域
@Retention(RetentionPolicy.CLASS)  //表示注解存在存在的时间
public @interface Route {

    /**
     * 路由的路径，标识一个路由的节点
     * @return
     */
    String path();

    /**
     * 将路由节点进行分组，可以实现按组动态加载
     * @return
     */
    String group() default "";

}
