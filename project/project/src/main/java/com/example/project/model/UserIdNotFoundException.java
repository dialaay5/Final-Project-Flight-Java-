package com.example.project.model;

public class UserIdNotFoundException extends ClientFaultException{
    public UserIdNotFoundException(String message) {
        super(message);
    }
}

