package com.mnnu.ams.Module;

public class User {
    private String name;
    private String email;
    private int pin;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getPin() {
        return pin;
    }

    public User(String name, String email, int pin) {
        this.name = name;
        this.email = email;
        this.pin = pin;
    }

    public User() {
    }
}
