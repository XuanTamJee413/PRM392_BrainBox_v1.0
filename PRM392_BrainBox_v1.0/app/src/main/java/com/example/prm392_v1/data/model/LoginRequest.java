package com.example.prm392_v1.data.model;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("usernameOrEmail")
    String usernameOrEmail;

    @SerializedName("password")
    String password;

    public LoginRequest(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }
}
