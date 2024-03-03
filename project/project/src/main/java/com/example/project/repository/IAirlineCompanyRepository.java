package com.example.project.repository;

import com.example.project.model.Administrator;
import com.example.project.model.AirlineCompany;
import com.example.project.model.ClientFaultException;

import java.util.List;

public interface IAirlineCompanyRepository {
    AirlineCompany addAirlineCompany(AirlineCompany airlineCompany) throws ClientFaultException;
    void addAllAirlineCompanies(List<AirlineCompany> airlineCompanies) throws ClientFaultException ;
    void updateAirlineCompany(AirlineCompany airlineCompany, Long id);

    void removeAirlineCompany(Long id);

    List<AirlineCompany> getAllAirlineCompanies();

    AirlineCompany getAirlineCompanyById(Long id);

    List<AirlineCompany> getAirlinesByCountry( Integer country_id);

    AirlineCompany get_airline_by_username(String user_name);
}
