package com.webapps.dictionary;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String password;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
