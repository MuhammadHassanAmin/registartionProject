package com.example.registartionproject;

public class UserModel {
    private  String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    private String name;
    private String email;
    private String dp;


    public UserModel(String id, String name, String email, String dp) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dp = dp;

    }


}
