package com.example.project.model;

public class TicketsException extends ClientFaultException{
    public TicketsException(String message) {
        super(message);
    }
}
