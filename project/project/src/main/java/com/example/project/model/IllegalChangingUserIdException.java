package com.example.project.model;

public class IllegalChangingUserIdException extends ClientFaultException{
    public IllegalChangingUserIdException(String message) {
        super(message);
    }
}
