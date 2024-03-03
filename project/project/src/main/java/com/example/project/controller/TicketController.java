package com.example.project.controller;

import com.example.project.model.ClientFaultException;
import com.example.project.model.Ticket;
import com.example.project.model.TicketsException;
import com.example.project.model.payload.response.MessageResponse;
import com.example.project.service.TicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/ticket")
public class TicketController {
    @Autowired
    TicketsService ticketsService;

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @GetMapping("/getAll")
    //get all tickets
    public ResponseEntity<?> getAllTickets() {
        try {
            List<Ticket> list = ticketsService.getAllTickets();
            return ResponseEntity.ok(list);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasAuthority('AIRLINE_COMPANY')")
    @GetMapping("/ticketById/{id}")
    //get ticket by id
    public ResponseEntity<?> getTicketById(@PathVariable("id") Long id) {
        try {
            Ticket ticket = ticketsService.getTicketById(id);
            if (ticket != null) {
                return ResponseEntity.ok(ticket);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found ticket with Id " + id));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/addTicketsList")
    //add all ticket
    public ResponseEntity<?> addAllTicket(@RequestBody List<Ticket> tickets){
        try{
            ticketsService.addAllTickets(tickets);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("New list of tickets has been added!"));
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PostMapping("/addTicket")
    //add ticket
    public ResponseEntity<?> addTicket(@RequestBody Ticket ticket){
        try{
            Ticket addTicket = ticketsService.addTicket(ticket);
            return ResponseEntity.status(HttpStatus.CREATED).body(addTicket);
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PutMapping("/updateTicket/{id}")
    //update ticket
    public ResponseEntity<?> updateTicket(@PathVariable("id") Long id, @RequestBody Ticket ticket) {
        try{
            ticketsService.updateTicket(ticket, id);
            return ResponseEntity.ok(ticket);
        }
        catch (TicketsException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (ClientFaultException | DuplicateKeyException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @DeleteMapping("/removeTicket/{id}")
    //remove ticket
    public ResponseEntity<?> removeTicket(@PathVariable("id") Long id){
        try {
            ticketsService.removeTicket(id);
            return ResponseEntity.ok(new MessageResponse("The ticket has been removed!"));
        }
        catch (TicketsException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
}
