package com.dn_alan.eventbus;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/18.
 */

public class Friend{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Friend(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
