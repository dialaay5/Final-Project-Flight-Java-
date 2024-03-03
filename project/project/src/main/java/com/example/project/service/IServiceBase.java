package com.example.project.service;

import com.example.project.model.AirlineCompany;
import com.example.project.model.Country;
import com.example.project.model.Flight;

import java.time.LocalDateTime;
import java.util.List;

public interface IServiceBase {
    List<Flight> getAllFlights();
    Flight getFlightById(Long id);
    List<Flight> get_flights_by_parameters(Integer originCountry_id, Integer destinationCountry_id, LocalDateTime departure_time);
    List<AirlineCompany> getAllAirlineCompanies();
    AirlineCompany getAirlineCompanyById(Long id);
    List<Country> getAllCountries();
    Country getCountryById(Integer id);
}
