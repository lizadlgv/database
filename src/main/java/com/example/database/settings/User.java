package com.example.database.settings;

public class User {
    private String name;

    private String password;


    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}