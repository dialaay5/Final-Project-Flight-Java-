package com.example.project.model;

public class FlightDateTimeException extends ClientFaultException{
    public FlightDateTimeException(String message) {
        super(message);
    }
}
