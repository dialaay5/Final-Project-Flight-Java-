package com.example.project.model;

public class CustomerNotFoundException extends ClientFaultException{
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
