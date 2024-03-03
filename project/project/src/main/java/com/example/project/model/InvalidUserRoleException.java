package com.example.project.model;

public class InvalidUserRoleException extends ClientFaultException{
    public InvalidUserRoleException(String message) {
        super(message);
    }
}