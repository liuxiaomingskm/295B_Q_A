package com.nowcoder.model;

public class User {
    public String name;
    public User(String name){
        this.name  = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return "This is " + name;
    }
}
