package com.example.project.repository;

import com.example.project.model.ClientFaultException;
import com.example.project.model.Flight;
import com.example.project.model.Ticket;

import java.util.List;

public interface ITicketRepository {
    Ticket addTicket(Ticket ticket) throws ClientFaultException;
    void addAllTickets(List<Ticket> tickets) throws ClientFaultException ;
    void updateTicket(Ticket ticket, Long id);

    void removeTicket(Long id);

    List<Ticket> getAllTickets();

    Ticket getTicketById(Long id);

    List<Ticket> get_tickets_by_customer(Long customer_id);
}
