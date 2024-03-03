package com.example.project.model;

public class InvalidPasswordException extends ClientFaultException{
    public InvalidPasswordException(String message) {
        super(message);
    }
}
