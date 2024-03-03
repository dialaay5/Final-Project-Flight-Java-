package com.example.project.model.payload.request;


import jakarta.validation.constraints.NotBlank;

@NotBlank
public class LoginRequest {
    private String user_name;
    private String password;

    public String getUser_name() {
        return user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
