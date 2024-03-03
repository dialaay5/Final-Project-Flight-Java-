package com.example.project.model;

public class TicketPurchaseException extends ClientFaultException{
    public TicketPurchaseException(String message) {
        super(message);
    }
}