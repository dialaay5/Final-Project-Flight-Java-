package com.example.project.repository;

import com.example.project.model.Administrator;
import com.example.project.model.ClientFaultException;
import com.example.project.model.Country;

import java.util.List;

public interface ICountriesRepository {
    Country addCountry(Country country) throws ClientFaultException;
    List<Country> addAllCountries(List<Country> countries) throws ClientFaultException;
    void updateCountry(Country country, Integer id);

    void removeCountry(Integer id);

    List<Country> getAllCountries();

    Country getCountryById(Integer id);
}
