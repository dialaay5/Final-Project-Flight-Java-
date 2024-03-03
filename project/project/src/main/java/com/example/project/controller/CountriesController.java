package com.example.project.controller;

import com.example.project.model.ClientFaultException;
import com.example.project.model.Country;
import com.example.project.model.CountryNotFoundException;
import com.example.project.model.payload.response.MessageResponse;
import com.example.project.service.CountriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/country")
public class CountriesController {
    @Autowired
    CountriesService countriesService;
    @GetMapping("/getAll")
    //get all countries
    public ResponseEntity<?> getAllCountries() {
        try {
            List<Country> list = countriesService.getAllCountries();
            return ResponseEntity.ok(list);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/countryById/{id}")
    //get country by id
    public ResponseEntity<?> countryById(@PathVariable("id") Integer id) {
        try {
            Country country = countriesService.getCountryById(id);
            if (country != null) {
                return ResponseEntity.ok(country);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found country with Id " + id));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PostMapping("/addCountriesList")
    //add all countries
    public ResponseEntity<?> addAllCountries(@RequestBody List<Country> countries){
        try{
            List<Country> addCountries = countriesService.addAllCountries(countries);
            return ResponseEntity.status(HttpStatus.CREATED).body(addCountries);
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PostMapping("/addCountry")
    //add userRule
    public ResponseEntity<?> addCountry(@RequestBody Country country){
        try{
            Country addCountry = countriesService.addCountry(country);
            return ResponseEntity.status(HttpStatus.CREATED).body(addCountry);
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PutMapping("/updateCountry/{id}")
    //update Country
    public ResponseEntity<?> updateCountry(@PathVariable("id") Integer id, @RequestBody Country country) {
        try{
            countriesService.updateCountry(country, id);
            return ResponseEntity.ok(country);
        }
        catch (CountryNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (ClientFaultException | DuplicateKeyException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @DeleteMapping("/removeCountry/{id}")
    //remove Country
    public ResponseEntity<?> removeCountry(@PathVariable("id") Integer id){
        try {
            countriesService.removeCountry(id);
            return ResponseEntity.ok(new MessageResponse("The country has been removed!"));
        }
        catch (ClientFaultException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
}


