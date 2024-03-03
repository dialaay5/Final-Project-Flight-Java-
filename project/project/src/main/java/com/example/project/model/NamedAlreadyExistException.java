package com.example.project.model;

public class NamedAlreadyExistException extends ClientFaultException{
    public NamedAlreadyExistException(String message) {
        super(message);
    }
}

