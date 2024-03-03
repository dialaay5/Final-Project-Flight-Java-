package com.example.project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirlineCompany {
    protected Long id;
    protected String airlineCompany_name;
    protected Integer country_id;

    protected Long user_id;

    public AirlineCompany() {
    }

    public AirlineCompany(Long id, String airlineCompany_name, Integer country_id, Long user_id) {
        this.id = id;
        this.airlineCompany_name = airlineCompany_name;
        this.country_id = country_id;
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "AirlineCompany{" +
                "id=" + id +
                ", airlineCompany_name='" + airlineCompany_name + '\'' +
                ", country_id=" + country_id +
                ", user_id=" + user_id +
                '}';
    }
}
