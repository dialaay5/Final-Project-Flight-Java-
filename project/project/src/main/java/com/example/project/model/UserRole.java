package com.example.project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRole {
    protected Integer id;
    protected UserRoleName role_name;

    public UserRole() {
    }

    public UserRole(Integer id, UserRoleName role_name) {
        this.id = id;
        this.role_name = role_name;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", role_name='" + role_name.name() + '\'' +
                '}';
    }
}
