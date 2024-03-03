package com.example.project.model.payload.response;

import com.example.project.model.User;
import lombok.Getter;

@Getter
public class JwtResponse {
    private User user;
    private String token;


    public JwtResponse(User user,String token) {
        this.user = user;
        this.token = token;

    }
}
