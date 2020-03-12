package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * Created by nowcoder on 2016/7/3.
 */
@Component
public class HostHolder { //hostholder的目的是做成控制反转 从而在其他地方也能随意访问
    private static ThreadLocal<User> users = new ThreadLocal<User>();  //如果是private User user的话 那只能表示一个变量 两个线程同时访问时就会出错
    //java的线程池概念 多个线程里面的user统一用users来表示，通过这个users接口访问各个user 也叫变量副本

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();;
    }
}
