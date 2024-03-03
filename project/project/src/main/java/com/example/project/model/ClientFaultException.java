package com.example.project.model;

import org.springframework.http.HttpStatus;

public class ClientFaultException extends Exception{
    public ClientFaultException(String message) {
        super(message);
    }
    HttpStatus httpStatus;
}
