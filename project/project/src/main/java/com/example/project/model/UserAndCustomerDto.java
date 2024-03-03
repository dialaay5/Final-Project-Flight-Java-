package com.example.project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAndCustomerDto {
    protected User user;
    protected Customer customer;

    public UserAndCustomerDto() {
    }

    public UserAndCustomerDto(User user, Customer customer) {
        this.user = user;
        this.customer = customer;
    }

    //Dto Response
    public UserAndCustomerDto response(User user, Customer customer){
        return new UserAndCustomerDto(user, customer);
    }
}
