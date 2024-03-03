package com.example.project.service;

import com.example.project.model.*;
import com.example.project.model.payload.response.MessageResponse;
import com.example.project.repository.CustomerRepository;
import com.example.project.repository.FlightRepository;
import com.example.project.repository.TicketRepository;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class TicketsService implements ITicketsService{
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;

    private void verifyFlightAvailability(Long flightId) throws ClientFaultException {
        Flight flight = flightRepository.getFlightById(flightId);
        if (flight == null) {
            throw new FlightNotFoundException(new MessageResponse("Flight not found.").getMessage());
        }
        if (flight.getRemaining_tickets() == 0) {
            throw new UnavailableTicketsException(new MessageResponse("Sorry, there are no more tickets available for this flight").getMessage());
        }
        if (flight.getDeparture_time().isBefore(LocalDateTime.now())) {
            throw new FlightDateTimeException(new MessageResponse("Sorry, the flight date time has passed").getMessage());
        }
    }

    @Override
    public void addAllTickets(List<Ticket> tickets) throws ClientFaultException {
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        for (Ticket ticket : tickets) {
            //verify if this ticket belongs to the currently connected customer
            String customerUsername = userRepository.getUserById(customerRepository.getCustomerById(ticket.getCustomer_id()).getUser_id()).getUser_name();
            if (connectedUsername.equals(customerUsername)) {
               verifyFlightAvailability(ticket.getFlight_id());
            }
        }
        //add ticket
        ticketRepository.addAllTickets(tickets);
        //update the remaining tickets for the specific flights
        for (Ticket ticket : tickets) {
            flightRepository.update_remaining_tickets_after_buying_ticket(ticket.getFlight_id());
        }
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.getAllTickets();
    }
    @Override
    public Ticket getTicketById(Long id) throws ClientFaultException {
        return ticketRepository.getTicketById(id);
    }

    @Override
    public Ticket addTicket(Ticket ticket) throws ClientFaultException {
        verifyFlightAvailability(ticket.getFlight_id());
        //buying ticket
        Ticket newTicket = ticketRepository.addTicket(ticket);
        //update the remaining tickets for a specific flight
        flightRepository.update_remaining_tickets_after_buying_ticket(ticket.getFlight_id());
        return newTicket;
    }


    @Override
    public void updateTicket(Ticket ticket, Long id) throws ClientFaultException {
        //verify if this is tha same ticket
        if (ticket.getId().equals(id)) {
            //verify if the ticket that requesting an update still exists
            Ticket existTicket = ticketRepository.getTicketById(id);
            if (existTicket == null) {
                throw new TicketsException(new MessageResponse("Ticket not found").getMessage());
            }
            //verify if flight ID is being changed
            Long flightIdBeforeUpdate = existTicket.getFlight_id();
            if (!flightIdBeforeUpdate.equals(ticket.getFlight_id())) {
                verifyFlightAvailability(ticket.getFlight_id());
                //update tickets and remaining tickets for both flights
                ticketRepository.updateTicket(ticket, id);
                flightRepository.update_remaining_tickets_after_returning_ticket(flightIdBeforeUpdate);
                flightRepository.update_remaining_tickets_after_buying_ticket(ticket.getFlight_id());
            } else {
                //update the ticket if flight ID remains the same
                ticketRepository.updateTicket(ticket, id);
            }
        }
        else {
            throw new ClientFaultException("provided a wrong data");
        }
    }

    @Override
    public void removeTicket(Long id) throws ClientFaultException {
        //verify if the ticket requested for removal still exists
        Ticket existTicket = ticketRepository.getTicketById(id);
        if (existTicket == null) {
            throw new TicketsException(new MessageResponse("Ticket not found").getMessage());
        }
        //get flight id
        Long ticketFlightId = existTicket.getFlight_id();
        //remove the customer ticket
        ticketRepository.removeTicket(id);
        //update the remaining tickets for a specific flight
        flightRepository.update_remaining_tickets_after_returning_ticket(ticketFlightId);
    }
}
