package com.example.project.model;

public class AdministratorNotFoundException extends ClientFaultException{
    public AdministratorNotFoundException(String message) {
        super(message);
    }
}
