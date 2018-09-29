package com.example.junmung.firebasechat;

public class UserData {
    private String name;
    private String message;
    private String uid;

    public UserData() {
    }

    public UserData(String name, String uid) {
        this.name = name;
        this.uid = uid;
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

    public String getUid() {
        return uid;
    }
}
