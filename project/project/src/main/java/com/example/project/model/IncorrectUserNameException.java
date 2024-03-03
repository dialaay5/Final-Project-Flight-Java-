package com.example.project.model;

public class IncorrectUserNameException extends ClientFaultException{
    public IncorrectUserNameException(String message) {
        super(message);
    }
}

