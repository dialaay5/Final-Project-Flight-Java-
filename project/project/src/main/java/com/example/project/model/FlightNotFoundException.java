package com.example.project.model;

public class FlightNotFoundException extends ClientFaultException{
    public FlightNotFoundException(String message) {
        super(message);
    }
}