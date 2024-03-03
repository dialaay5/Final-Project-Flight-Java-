package com.example.project.service;

import com.example.project.model.*;

import java.util.List;

public interface ICustomerService {
    void updateCustomer(Customer customer, Long id) throws ClientFaultException;
    Customer getCustomerById(Long id) throws ClientFaultException;
    Customer get_customer_by_username(String user_name) throws ClientFaultException;
    Ticket addTicket(Ticket ticket) throws ClientFaultException;
    void updateTicket(Ticket ticket, Long id) throws ClientFaultException;
    void removeTicket(Long id) throws ClientFaultException;
    List<Ticket> get_tickets_by_customer(Long customer_id) throws ClientFaultException;
    List<Flight> getFlightsByCustomer(Long customer_id) throws ClientFaultException;
    void removeCustomer(Long id) throws ClientFaultException;
    UserAndCustomerDto addCustomerUser(UserAndCustomerDto userAndCustomerDto) throws ClientFaultException;


}
