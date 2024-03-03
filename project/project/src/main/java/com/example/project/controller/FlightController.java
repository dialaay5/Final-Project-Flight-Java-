package com.example.project.controller;

import com.example.project.model.*;
import com.example.project.model.payload.response.MessageResponse;
import com.example.project.service.FlightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/flight")
public class FlightController {
    @Autowired
    FlightsService flightsService;

    @GetMapping("/getAll")
    //get all flight
    public ResponseEntity<?> getAllFlight() {
        try {
            List<Flight> list = flightsService.getAllFlights();
            return ResponseEntity.ok(list);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @GetMapping("/flightById/{id}")
    //get flight by id
    public ResponseEntity<?> getFlightById(@PathVariable("id") Long id) {
        try {
            Flight flight = flightsService.getFlightById(id);
            if (flight != null) {
                return ResponseEntity.ok(flight);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found flight with Id " + id));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/flightsByOriginCountryId/{id}")
    //get flights by originCountry id
    public ResponseEntity<?> getFlightsByOriginCountryId(@PathVariable("id") Integer id){
        try {
            List<Flight> flightsList = flightsService.getFlightsByOriginCountryId(id);
            if(flightsList != null){
                return ResponseEntity.ok(flightsList);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found country with Id " + id));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/flightsByDestinationCountryId/{id}")
    //get flights by destinationCountry id
    public ResponseEntity<?> getFlightsByDestinationCountryId(@PathVariable("id") Integer id){
        try {
            List<Flight> flightsList = flightsService.getFlightsByDestinationCountryId(id);
            if (flightsList != null) {
                return ResponseEntity.ok(flightsList);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found country with Id " + id));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/flightsByDepartureDate/{departure_time}")
    //get flights by departureDate
    //example "http://localhost:8087/api/flight/flightsByDepartureDate/2023-08-01T12:00:00"
    public ResponseEntity<?> getFlightsByDepartureDate(@PathVariable LocalDateTime departure_time){
        try {
            List<Flight> flightsList = flightsService.getFlightsByDepartureDate(departure_time);
            if (!flightsList.isEmpty()){
                return ResponseEntity.ok(flightsList);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No flights found for this departure time " + departure_time + ",It doesn't exist"));
        }
         catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/flightsByLandingDate/{landing_time}")
    //get flights by landingDate
    public ResponseEntity<?> getFlightsByLandingDate(@PathVariable LocalDateTime landing_time){
        try {
            List<Flight> flightsList = flightsService.getFlightsByLandingDate(landing_time);
            if (!flightsList.isEmpty()){
                return ResponseEntity.ok(flightsList);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No flights found for this arrival time " + landing_time + ",It doesn't exist"));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/flightsByParameters/{originCountry_id}/{destinationCountry_id}/{departure_time}")
    //get flights by parameters
    //example "http://localhost:8087/api/auth/flightsByParameters/1/2/2023-08-01T12:00:00"
    public ResponseEntity<?> getFlightsByParameters(@PathVariable  Integer originCountry_id,@PathVariable Integer destinationCountry_id,@PathVariable LocalDateTime departure_time){
        try {
            List<Flight> flightsList = flightsService.get_flights_by_parameters(originCountry_id, destinationCountry_id, departure_time);
            if (!flightsList.isEmpty()) {
                return ResponseEntity.ok(flightsList);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No flights found with this parameters " + originCountry_id + destinationCountry_id + departure_time));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/arrivalFlights/{id}")
    //get arrivalFlights by destinationCountry id
    public ResponseEntity<?> get_arrival_flights(@PathVariable("id") Integer destinationCountry_id){
        try {
            List<Flight> flightsList = flightsService.get_arrival_flights(destinationCountry_id);
            if(flightsList != null){
                return ResponseEntity.ok(flightsList);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No flights found with this country Id " + destinationCountry_id));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/departureFlights/{id}")
    //get departureFlights by originCountry  id
    public ResponseEntity<?> get_departure_flights(@PathVariable("id") Integer originCountry_id){
        try {
            List<Flight> flightsList = flightsService.get_departure_flights(originCountry_id);
            if(flightsList != null){
                return ResponseEntity.ok(flightsList);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No flights found with this country Id " + originCountry_id));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('AIRLINE_COMPANY')")
    @PostMapping("/addFlightsList")
    //add all flights
    public ResponseEntity<?> addAllFlights(@RequestBody List<Flight> flights){
        try{
            flightsService.addAllFlights(flights);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("New list of flights has been added!"));
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PostMapping("/addFlight")
    //add flight
    public ResponseEntity<?> addFlight(@RequestBody Flight flight){
        try{
            Flight addFlight = flightsService.addFlight(flight);
            return ResponseEntity.status(HttpStatus.CREATED).body(addFlight);
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PutMapping("/updateFlight/{id}")
    //update flight
    public ResponseEntity<?> updateFlight(@PathVariable("id") Long id, @RequestBody Flight flight) {
        try{
            flightsService.updateFlight(flight, id);
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
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @DeleteMapping("/removeFlight/{id}")
    //remove flight
    public ResponseEntity<?> removeFlight(@PathVariable("id") Long id){
        try {
            flightsService.removeFlight(id);
            return ResponseEntity.ok(new MessageResponse("The flight has been removed!"));
        }
        catch (FlightNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
}

