package com.example.project.service;

import com.example.project.model.ClientFaultException;
import com.example.project.model.Flight;

import java.time.LocalDateTime;
import java.util.List;

public interface IFlightsService {
    void addAllFlights(List<Flight> flights) throws ClientFaultException;
    List<Flight> getAllFlights();
    Flight addFlight(Flight flight) throws ClientFaultException;
    void updateFlight(Flight flight, Long id) throws ClientFaultException;
    void removeFlight(Long id) throws ClientFaultException;
    Flight getFlightById(Long id) throws ClientFaultException;
    List<Flight> getFlightsByOriginCountryId(Integer originCountry_id);
    List<Flight> getFlightsByDestinationCountryId(Integer destinationCountry_id);
    List<Flight> getFlightsByDepartureDate(LocalDateTime departure_time);
    List<Flight> getFlightsByLandingDate(LocalDateTime landing_time);
    List<Flight> get_flights_by_parameters(Integer originCountry_id, Integer destinationCountry_id, LocalDateTime departure_time);
    List<Flight> get_arrival_flights(Integer destinationCountry_id);
    List<Flight> get_departure_flights(Integer originCountry_id);
}