package com.example.project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Administrator {
    protected Integer id;
    protected String administrator_firstName;
    protected String administrator_lastName;
    protected Long user_id;

    public Administrator() {
    }
    public Administrator(Integer id, String administrator_firstName, String administrator_lastName, Long user_id) {
        this.id = id;
        this.administrator_firstName = administrator_firstName;
        this.administrator_lastName = administrator_lastName;
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "id=" + id +
                ", administrator_firstName='" + administrator_firstName + '\'' +
                ", administrator_lastName='" + administrator_lastName + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
