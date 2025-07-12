package com.example.prm392_v1.data.model;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("usernameOrEmail")
    public String username;

    @SerializedName("password")
    public String password;

    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
