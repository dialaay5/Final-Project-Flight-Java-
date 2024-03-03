package com.example.project.model;

public class UserNameOrEmailAlreadyExistsException extends ClientFaultException{
    public UserNameOrEmailAlreadyExistsException(String message) {
        super(message);
    }
}
