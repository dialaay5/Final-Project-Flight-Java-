package com.example.project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {
    protected Long id;
    protected String customer_firstName;
    protected String customer_lastName;
    protected String address;
    protected String phone_number;
    protected String creditCard_number;
    protected Long user_id;

    public Customer() {
    }

    public Customer(Long id, String customer_firstName, String customer_lastName, String address, String phone_number, String creditCard_number, Long user_id) {
        this.id = id;
        this.customer_firstName = customer_firstName;
        this.customer_lastName = customer_lastName;
        this.address = address;
        this.phone_number = phone_number;
        this.creditCard_number = creditCard_number;
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", customer_firstName='" + customer_firstName + '\'' +
                ", customer_lastName='" + customer_lastName + '\'' +
                ", address='" + address + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", creditCard_number='" + creditCard_number + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
