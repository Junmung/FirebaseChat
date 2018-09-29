package com.example.junmung.firebasechat;

public class UserData {
    private String name;
    private String message;

    public UserData() {
    }

    public UserData(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

}
