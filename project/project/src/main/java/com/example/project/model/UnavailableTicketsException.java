package com.example.project.model;

public class UnavailableTicketsException extends ClientFaultException{
    public UnavailableTicketsException(String message) {
        super(message);
    }
}