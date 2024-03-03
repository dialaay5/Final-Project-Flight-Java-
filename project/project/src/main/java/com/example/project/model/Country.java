package com.example.project.model;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class Country {
    protected Integer id;
    protected String country_name;

    public Country() {
    }

    public Country(Integer id, String country_name) {
        this.id = id;
        this.country_name = country_name;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", country_name='" + country_name + '\'' +
                '}';
    }
}
