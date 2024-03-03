package com.example.project.repository;

import com.example.project.model.ClientFaultException;
import com.example.project.model.Customer;
import com.example.project.model.Flight;

import java.time.LocalDateTime;
import java.util.List;

public interface IFlightRepository {
    Flight addFlight(Flight flight) throws ClientFaultException;
    void addAllFlights(List<Flight> flights) throws ClientFaultException;

    void updateFlight(Flight flight, Long id);

    void removeFlight(Long id);

    List<Flight> getAllFlights();

    Flight getFlightById(Long id);

    List<Flight> getFlightsByOriginCountryId(Integer originCountry_id);

    List<Flight> getFlightsByDestinationCountryId(Integer destinationCountry_id);

    List<Flight> getFlightsByDepartureDate(LocalDateTime departure_time);

    List<Flight> getFlightsByLandingDate(LocalDateTime landing_time);

    List<Flight> getFlightsByCustomer(Long customer_id);

    List<Flight> get_flights_by_parameters(Integer originCountry_id,Integer destinationCountry_id,LocalDateTime departure_time);

    List<Flight> get_flights_by_airline_id(Long airlineCompany_id);

    List<Flight> get_arrival_flights(Integer destinationCountry_id);

    List<Flight> get_departure_flights(Integer originCountry_id);

    void update_remaining_tickets_after_buying_ticket(Long flightId);

    void update_remaining_tickets_after_returning_ticket(Long id);
    }
