package com.example.project.service;

import com.example.project.model.ClientFaultException;
import com.example.project.model.Ticket;
import com.example.project.model.TicketsException;

import java.util.List;

public interface ITicketsService {
    void addAllTickets(List<Ticket> tickets) throws ClientFaultException ;
    List<Ticket> getAllTickets();
    Ticket getTicketById(Long id) throws ClientFaultException;
    Ticket addTicket(Ticket ticket) throws ClientFaultException;
    void updateTicket(Ticket ticket, Long id) throws ClientFaultException;
    void removeTicket(Long id) throws ClientFaultException;
}
