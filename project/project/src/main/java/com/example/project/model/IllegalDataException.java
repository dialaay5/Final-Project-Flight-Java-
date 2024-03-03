package com.example.project.model;

public class IllegalDataException extends ClientFaultException{
    public IllegalDataException(String message) {
        super(message);
    }
}

