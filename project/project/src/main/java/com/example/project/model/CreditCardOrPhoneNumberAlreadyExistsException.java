package com.example.project.model;

public class CreditCardOrPhoneNumberAlreadyExistsException extends ClientFaultException{
    public CreditCardOrPhoneNumberAlreadyExistsException(String message) {
        super(message);
    }
}
