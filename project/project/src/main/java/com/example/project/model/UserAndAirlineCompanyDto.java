package com.example.project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAndAirlineCompanyDto {
    protected User user;
    protected AirlineCompany airlineCompany;

    public UserAndAirlineCompanyDto() {
    }

    public UserAndAirlineCompanyDto(User user, AirlineCompany airlineCompany) {
        this.user = user;
        this.airlineCompany = airlineCompany;
    }

    //Dto Response
    public UserAndAirlineCompanyDto response(User user,AirlineCompany airlineCompany){
        return new UserAndAirlineCompanyDto(user, airlineCompany);
    }
}
