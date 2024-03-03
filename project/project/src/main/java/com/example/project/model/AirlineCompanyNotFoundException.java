package com.example.project.model;

public class AirlineCompanyNotFoundException extends ClientFaultException{
    public AirlineCompanyNotFoundException(String message) {
        super(message);
    }
}