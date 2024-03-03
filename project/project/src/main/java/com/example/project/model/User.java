package com.example.project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    protected Long id;
    protected String user_name;
    protected String password;
    protected String email;
    protected Integer user_role;

    public User() {
    }

    public User(Long id, String user_name, String password, String email, Integer user_role ) {
        this.id = id;
        this.user_name = user_name;
        this.password = password;
        this.email = email;
        this.user_role = user_role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", user_name='" + user_name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", user_role=" + user_role +
                '}';
    }
}
