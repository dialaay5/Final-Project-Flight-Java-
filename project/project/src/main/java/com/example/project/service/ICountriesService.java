package com.example.project.service;

import com.example.project.model.ClientFaultException;
import com.example.project.model.Country;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface ICountriesService {
    Country addCountry(Country country) throws ClientFaultException;
    List<Country> addAllCountries(List<Country> countries) throws ClientFaultException, JsonProcessingException;
    void updateCountry(Country country, Integer id) throws ClientFaultException, JsonProcessingException;

    void removeCountry(Integer id) throws ClientFaultException, JsonProcessingException;

    List<Country> getAllCountries();

    Country getCountryById(Integer id);
}
