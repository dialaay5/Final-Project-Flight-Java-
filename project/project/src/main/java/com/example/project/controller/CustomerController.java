package com.example.project.controller;

import com.example.project.model.*;
import com.example.project.model.payload.response.MessageResponse;
import com.example.project.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/customer")
@PreAuthorize("hasAuthority('CUSTOMER')")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping("/customerById/{id}")
    //get customer by id
    public ResponseEntity<?> getCustomerById(@PathVariable("id") Long id) {
        try {
            Customer customer = customerService.getCustomerById(id);
            if (customer != null) {
                return ResponseEntity.ok(customer);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found customer with Id " + id));
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/ticketsByCustomerId/{id}")
    //get tickets by customer id
    public ResponseEntity<?> getTicketsByCustomerId(@PathVariable("id") Long id){
        try {
            List<Ticket> ticketsList = customerService.get_tickets_by_customer(id);
            return ResponseEntity.ok(ticketsList);
        }
        catch (CustomerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/flightsByCustomerId/{id}")
    //get flights by customer id
    public ResponseEntity<?> getFlightsByCustomerId(@PathVariable("id") Long id){
        try {
            List<Flight> flightsList = customerService.getFlightsByCustomer(id);
            return ResponseEntity.ok(flightsList);
        }
        catch (CustomerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/customerByUsername/{user_name}")
    //get customer by username
    public ResponseEntity<?> getCustomerByUsername(@PathVariable String user_name){
        try {
            Customer customer = customerService.get_customer_by_username(user_name);
            if (customer != null) {
                return ResponseEntity.ok(customer);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found customer with username " + user_name));
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @PostMapping("/addTicket")
    //add ticket
    public ResponseEntity<?> addTicket(@RequestBody Ticket ticket){
        try{
            Ticket addTicket = customerService.addTicket(ticket);
            return ResponseEntity.status(HttpStatus.CREATED).body(addTicket);
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @PutMapping("/updateCustomer/{id}")
    //update customer
    public ResponseEntity<?> updateCustomer(@PathVariable("id") Long id, @RequestBody Customer customer) {
        try{
            customerService.updateCustomer(customer, id);
            return ResponseEntity.ok(customer);
        }
        catch (CustomerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (ClientFaultException | DuplicateKeyException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @PutMapping("/updateTicket/{id}")
    //update ticket
    public ResponseEntity<?> updateTicket(@PathVariable("id") Long id, @RequestBody Ticket ticket) {
        try{
            customerService.updateTicket(ticket, id);
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

    @DeleteMapping("/removeTicket/{id}")
    //remove ticket
    public ResponseEntity<?> removeTicket(@PathVariable("id") Long id){
        try {
            customerService.removeTicket(id);
            return ResponseEntity.ok(new MessageResponse("The ticket has been removed!"));
        }
        catch (TicketsException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (ClientFaultException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @PostMapping("/addCustomerUser")
    //add customerUser
    public ResponseEntity<?> addCustomerUser(@RequestBody UserAndCustomerDto userAndCustomerDto){
        try{
            UserAndCustomerDto customerUser = customerService.addCustomerUser(userAndCustomerDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(customerUser);
        }
        catch (ClientFaultException | DuplicateKeyException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/removeCustomer/{id}")
    //remove customer
    public ResponseEntity<?> removeCustomer(@PathVariable("id") Long id){
        try {
            customerService.removeCustomer(id);
            return ResponseEntity.ok(new MessageResponse("The customer and its user have been removed!"));
        }
        catch (CustomerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/getAllAirlines")
    //get all airline companies
    public ResponseEntity<?> getAllAirlineCompanies() {
        try {
            List<AirlineCompany> list = customerService.getAllAirlineCompanies();
            return ResponseEntity.ok(list);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/getAirlineById/{id}")
    //get airline by id
    public ResponseEntity<?> getAirlineById(@PathVariable("id") Long id) {
        try {
            AirlineCompany airlineCompany = customerService.getAirlineCompanyById(id);
            if (airlineCompany != null) {
                return ResponseEntity.ok(airlineCompany);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found airline company with Id " + id));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
}
