package com.company.shared.entity;

import com.company.shared.annotations.Field;

import java.io.Serializable;

public class User implements Serializable {
    @Field(type = "String") private String username;
    @Field(type = "String") private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
