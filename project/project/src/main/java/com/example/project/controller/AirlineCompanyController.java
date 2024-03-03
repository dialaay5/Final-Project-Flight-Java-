package com.example.project.controller;

import com.example.project.model.*;
import com.example.project.model.payload.response.MessageResponse;
import com.example.project.service.AirlineCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/airlineCompany")
public class AirlineCompanyController {
    @Autowired
    AirlineCompanyService airlineCompanyService;
    @PreAuthorize("hasAuthority('AIRLINE_COMPANY')")
    @GetMapping("/getAll")
    //get all airlines
    public ResponseEntity<?> getAllAirlines() {
        try {
            List<AirlineCompany> list = airlineCompanyService.getAllAirlineCompanies();
            return ResponseEntity.ok(list);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('AIRLINE_COMPANY')")
    @GetMapping("/airlineById/{id}")
    //get airline by id
    public ResponseEntity<?> airlineById(@PathVariable("id") Long id) {
        try {
            AirlineCompany airlineCompany = airlineCompanyService.getAirlineCompanyById(id);
            if (airlineCompany != null) {
                return ResponseEntity.ok(airlineCompany);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found airline company with Id " + id));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('AIRLINE_COMPANY')")
    @GetMapping("/flightsByAirlineId/{id}")
    //get flights by airline company id
    public ResponseEntity<?> getFlightsByAirlineId(@PathVariable("id") Long id){
        try {
            List<Flight> flightsList = airlineCompanyService.get_flights_by_airline_id(id);
            if(flightsList != null){
                return ResponseEntity.ok(flightsList);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found airline company with Id " + id));
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('AIRLINE_COMPANY')")
    @GetMapping("/airlineByUsername/{user_name}")
    //get airline by username
    public ResponseEntity<?> getAirlineByUsername(@PathVariable String user_name){
        try {
            AirlineCompany airlineCompany = airlineCompanyService.get_airline_by_username(user_name);
            if (airlineCompany != null) {
                return ResponseEntity.ok(airlineCompany);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found airline company with username " + user_name));
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasAuthority('AIRLINE_COMPANY') or hasAuthority('CUSTOMER')")
    @GetMapping("/airlinesByCountryId/{id}")
    //get airlines by country id
    public ResponseEntity<?> getAirlinesByCountryId(@PathVariable("id") Integer id){
        try {
            List<AirlineCompany> airlineCompaniesList = airlineCompanyService.getAirlinesByCountry(id);
            if(airlineCompaniesList != null){
                return ResponseEntity.ok(airlineCompaniesList);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found country with Id " + id));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('AIRLINE_COMPANY')")
    @PutMapping("/updateAirline/{id}")
    //update airline
    public ResponseEntity<?> updateAirline(@PathVariable("id") Long id, @RequestBody AirlineCompany airlineCompany) {
        try{
            airlineCompanyService.updateAirlineCompany(airlineCompany, id);
            return ResponseEntity.ok(airlineCompany);
        }
        catch (AirlineCompanyNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (ClientFaultException | DuplicateKeyException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('AIRLINE_COMPANY')")
    @PutMapping("/updateFlight/{id}")
    //update flight
    public ResponseEntity<?> updateFlight(@PathVariable("id") Long id, @RequestBody Flight flight) {
        try{
            airlineCompanyService.updateFlight(flight, id);
            return ResponseEntity.ok(flight);
        }
        catch (FlightNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('AIRLINE_COMPANY')")
    @PostMapping("/addFlight")
    //add flight
    public ResponseEntity<?> addFlight(@RequestBody Flight flight){
        try{
            Flight addFlight = airlineCompanyService.addFlight(flight);
            return ResponseEntity.status(HttpStatus.CREATED).body(addFlight);
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('AIRLINE_COMPANY')")
    @DeleteMapping("/removeFlight/{id}")
    //remove flight
    public ResponseEntity<?> removeFlight(@PathVariable("id") Long id){
        try {
            airlineCompanyService.removeFlight(id);
            return ResponseEntity.ok(new MessageResponse("The flight has been removed!"));
        }
        catch (FlightNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('AIRLINE_COMPANY')")
    @DeleteMapping("/removeAirlineCompany/{id}")
    //remove airline company
    public ResponseEntity<?> removeAirlineCompany(@PathVariable("id") Long id){
        try {
            airlineCompanyService.removeAirlineCompany(id);
            return ResponseEntity.ok(new MessageResponse("The airline company and its user have been removed!"));
        }
        catch (AirlineCompanyNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('AIRLINE_COMPANY')")
    @PostMapping("/addAirlineCompanyUser")
    //add airlineCompanyUser
    public ResponseEntity<?> addAirlineCompanyUser(@RequestBody UserAndAirlineCompanyDto userAndAirlineCompanyDto){
        try{
            UserAndAirlineCompanyDto airlineCompanyUser = airlineCompanyService.addAirlineCompanyUser(userAndAirlineCompanyDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(airlineCompanyUser);
        }
        catch (ClientFaultException | DuplicateKeyException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
}
