package com.example.project.model;

public class CountryNotFoundException extends ClientFaultException{
    public CountryNotFoundException(String message) {
        super(message);
    }
}