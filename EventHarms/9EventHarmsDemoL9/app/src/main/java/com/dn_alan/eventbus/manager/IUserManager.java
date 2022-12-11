package com.dn_alan.eventbus.manager;


import com.dn_alan.eventbus.Friend;
import com.dn_alan.eventbus.annotion.ClassId;

//接口的方式  描述 一个类
@ClassId("com.dn_alan.eventbus.manager.UserManager")
public interface IUserManager {

    public Friend getFriend();

    public void setFriend(Friend friend);
}
