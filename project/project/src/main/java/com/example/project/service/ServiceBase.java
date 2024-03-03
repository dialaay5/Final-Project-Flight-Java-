package com.example.project.service;

import com.example.project.model.AirlineCompany;
import com.example.project.model.Country;
import com.example.project.model.Customer;
import com.example.project.model.Flight;
import com.example.project.repository.AirlineCompanyRepository;
import com.example.project.repository.CacheRepositoryImpl;
import com.example.project.repository.CountriesRepository;
import com.example.project.repository.FlightRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public abstract class ServiceBase implements IServiceBase {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirlineCompanyRepository airlineCompanyRepository;
    @Autowired
    private CountriesRepository countriesRepository;
    @Autowired
    private CacheRepositoryImpl cacheRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${cache_on}")
    private Boolean cache_on;
    private static final String airlineCompaniesKey = "airlineCompaniesList";

    public List<Flight> getAllFlights() {
        return flightRepository.getAllFlights();
    }

    public Flight getFlightById(Long id) {
        return flightRepository.getFlightById(id);
    }

    public List<Flight> get_flights_by_parameters(Integer originCountry_id, Integer destinationCountry_id, LocalDateTime departure_time) {
        return flightRepository.get_flights_by_parameters(originCountry_id, destinationCountry_id, departure_time);
    }

    public List<AirlineCompany> getAllAirlineCompanies() {
        try {
            //check if the Key is exists
            if (cache_on && cacheRepository.isKeyExist(airlineCompaniesKey)) {
                System.out.println("is Key Exist? " + cacheRepository.isKeyExist(airlineCompaniesKey));
                String airlineCompanies = cacheRepository.getCacheEntity(airlineCompaniesKey);
                System.out.println("reading from airlineCompanies cache " + airlineCompanies);
                return objectMapper.readValue(airlineCompanies, new TypeReference<List<AirlineCompany>>() {});
            }
            //if not..
            List<AirlineCompany> allAirlineCompanies = airlineCompanyRepository.getAllAirlineCompanies();
            if(allAirlineCompanies != null) {
                if (cache_on) {
                    cacheRepository.createCacheEntity(airlineCompaniesKey, objectMapper.writeValueAsString(allAirlineCompanies));
                }
            }
            return allAirlineCompanies;

        } catch (JsonProcessingException e) {
            System.out.println(e);
            throw new IllegalStateException("cannot write json of all airline companies");
        }
    }


    public AirlineCompany getAirlineCompanyById(Long id) {
        return airlineCompanyRepository.getAirlineCompanyById(id);
    }

    public List<Country> getAllCountries() {
        return countriesRepository.getAllCountries();
    }

    public Country getCountryById(Integer id) {
        return countriesRepository.getCountryById(id);
    }
}
