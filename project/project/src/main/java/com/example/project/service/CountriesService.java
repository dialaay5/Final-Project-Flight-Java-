package com.example.project.service;

import com.example.project.model.ClientFaultException;
import com.example.project.model.Country;
import com.example.project.model.CountryNotFoundException;
import com.example.project.repository.CacheRepositoryImpl;
import com.example.project.repository.CountriesRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CountriesService implements ICountriesService {
    @Autowired
    private CountriesRepository countriesRepository;
    @Autowired
    private CacheRepositoryImpl cacheRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${cache_on}")
    private Boolean cache_on;

    private static final String countriesKey = "CountryList";

    @Override
    public Country addCountry(Country country) throws ClientFaultException {
        try {
            //add a new country
            Country newCountry = countriesRepository.addCountry(country);

            //add the newly added country to the cache
            if (cache_on && cacheRepository.isKeyExist(countriesKey)) {
                List<Country> countries = getAllCountries();
                countries.add(newCountry);
                System.out.println("The cache has been updated with the new country that was added");
                cacheRepository.updateCountryCacheEntity(countriesKey, objectMapper.writeValueAsString(countries));
            }
            return newCountry;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Country> addAllCountries(List<Country> countries) throws ClientFaultException, JsonProcessingException {
        //add a list of new countries
        List<Country> newCountries = countriesRepository.addAllCountries(countries);

        //update the cache with the recently added list of countries
        if (cache_on && cacheRepository.isKeyExist(countriesKey)) {
            List<Country> countriesList = getAllCountries();
            countriesList.addAll(newCountries);
            System.out.println("The cache has been updated with the new countries list that was added");
            cacheRepository.updateCountryCacheEntity(countriesKey, objectMapper.writeValueAsString(countriesList));
        }
        return newCountries;
    }

    @Override
    public void updateCountry(Country country, Integer id) throws ClientFaultException, JsonProcessingException {
        //verify if this is tha same country
        if (country.getId().equals(id)) {
            //verify if the country that requested update still exists
            Country existCountry = countriesRepository.getCountryById(id);
            if (existCountry != null) {
                countriesRepository.updateCountry(country, id);

                //ensure that the cache is updated following any updates made to the country
                if (cache_on && cacheRepository.isKeyExist(countriesKey)) {
                    List<Country> countries = getAllCountries();
                    for(int index = 0; index < countries.size(); index++) {
                        Country countryFromCache = countries.get(index);
                        if (countryFromCache.getId().equals(existCountry.getId())) {
                            //found, update it
                            System.out.println(index);
                            countryFromCache.setCountry_name(country.getCountry_name());
                            break;
                        }
                    }
                    System.out.println("The cache has been updated after the country that was updated");
                    cacheRepository.updateCountryCacheEntity(countriesKey, objectMapper.writeValueAsString(countries));
                }
            } else {
                throw new CountryNotFoundException("country not found");
            }
        }
        else {
            throw new ClientFaultException("provided a wrong data");
        }
    }

    @Override
    public void removeCountry(Integer id) throws ClientFaultException, JsonProcessingException {
        //verify if the country that requested removal still exists
        Country existCountry = countriesRepository.getCountryById(id);
        if(existCountry != null){
            countriesRepository.removeCountry(id);

            //update the cache after removing the country
            if (cache_on && cacheRepository.isKeyExist(countriesKey)) {
                List<Country> countries = getAllCountries();
                for(int index = 0; index < countries.size(); index++) {
                    Country country = countries.get(index);
                    if (country.getId().equals(existCountry.getId())) {
                        //found, remove it
                        System.out.println(index);
                        countries.remove(index);
                        break;
                    }
                }
                System.out.println("The cache has been updated after the country that was removed");
                cacheRepository.updateCountryCacheEntity(countriesKey, objectMapper.writeValueAsString(countries));
            }
        }
        else{
            throw new ClientFaultException("country not found");
        }
    }


    @Override
    public List<Country> getAllCountries() {
        try {
            //check if the Key is exists
            if (cache_on && cacheRepository.isKeyExist(countriesKey)) {
                System.out.println("is Key Exist? " + cacheRepository.isKeyExist(countriesKey));
                String countries = cacheRepository.getCacheEntity(countriesKey);
                System.out.println("reading from cache " + countries);
                return objectMapper.readValue(countries, new TypeReference<List<Country>>() {});
            }
            //if not..
            List<Country> allCountries = countriesRepository.getAllCountries();
            if(allCountries != null) {
                if (cache_on) {
                    cacheRepository.createCountryCacheEntity(countriesKey, objectMapper.writeValueAsString(allCountries));
                }
            }
            return allCountries;

        } catch (JsonProcessingException e) {
            System.out.println(e);
            throw new IllegalStateException("cannot write json of allCountries");
        }
    }

    @Override
    public Country getCountryById(Integer id) {
        return countriesRepository.getCountryById(id);
    }
}
