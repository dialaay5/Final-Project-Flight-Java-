package com.example.project.service;

import com.example.project.model.AirlineCompany;
import com.example.project.model.ClientFaultException;
import com.example.project.model.Flight;
import com.example.project.model.UserAndAirlineCompanyDto;

import java.util.List;

public interface IAirlineCompanyService {
    List<Flight> get_flights_by_airline_id(Long airlineCompany_id) throws ClientFaultException;
    void updateAirlineCompany(AirlineCompany airlineCompany, Long id) throws ClientFaultException;
    Flight addFlight(Flight flight) throws ClientFaultException;
    void updateFlight(Flight flight, Long id) throws ClientFaultException;
    void removeFlight(Long id) throws ClientFaultException;
    AirlineCompany get_airline_by_username(String user_name) throws ClientFaultException;
    List<AirlineCompany> getAirlinesByCountry( Integer country_id);
    UserAndAirlineCompanyDto addAirlineCompanyUser (UserAndAirlineCompanyDto userAndAirlineCompanyDto) throws ClientFaultException;
    void removeAirlineCompany(Long id) throws ClientFaultException;


}
