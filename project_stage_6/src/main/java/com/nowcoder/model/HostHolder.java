package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * Created by nowcoder on 2016/7/3.
 */
@Component
/*
ThreadLocal variables differ from their normal counterparts in that each thread that accesses one (via its get or set method)
has its own, independently initialized copy of the variable. ThreadLocal instances are typically private static fields
in classes that wish to associate state with a thread (e.g., a user ID or Transaction ID).
 */
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<User>();//每一个访问的用户他的线程里面都是自己的用户（讲解原词，虽然有点莫名）

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
