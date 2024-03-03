package com.example.project.controller;

import com.example.project.model.*;
import com.example.project.model.payload.response.MessageResponse;
import com.example.project.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/administrator")
@PreAuthorize("hasAuthority('ADMINISTRATOR')")
public class AdministratorController {
    @Autowired
    AdministratorService administratorService;

    @GetMapping("/getAll")
    //get all administrators
    public ResponseEntity<?> getAllAdministrators() {
        try {
            List<Administrator> list = administratorService.getAllAdministrators();
            return ResponseEntity.ok(list);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/administratorById/{id}")
    //get administrator by id
    public ResponseEntity<?> getAdministratorById(@PathVariable("id") Integer id) {
        try {
            Administrator administrator = administratorService.getAdministratorById(id);
            if (administrator != null) {
                return ResponseEntity.ok(administrator);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found administrator with Id " + id));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/getAllCustomers")
    //get all customers
    public ResponseEntity<?> getAllCustomers() {
        try {
            List<Customer> list = administratorService.getAllCustomers();
            return ResponseEntity.ok(list);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/getAllAirlineCompanies")
    //get all airline companies
    public ResponseEntity<?> getAllAirlineCompanies() {
        try {
            List<AirlineCompany> list = administratorService.getAllAirlineCompanies();
            return ResponseEntity.ok(list);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/getAirlineById/{id}")
    //get airline by id
    public ResponseEntity<?> getAirlineById(@PathVariable("id") Long id) {
        try {
            AirlineCompany airlineCompany = administratorService.getAirlineCompanyById(id);
            if (airlineCompany != null) {
                return ResponseEntity.ok(airlineCompany);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found airline company with Id " + id));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PutMapping("/updateAdministrator/{id}")
    //update Administrator
    public ResponseEntity<?> updateAdministrator(@PathVariable("id") Integer id, @RequestBody Administrator administrator) {
        try{
            administratorService.updateAdministrator(administrator, id);
            return ResponseEntity.ok(administrator);
        }
        catch (AdministratorNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (ClientFaultException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/removeAirlineCompany/{id}")
    //remove airline company
    public ResponseEntity<?> removeAirlineCompany(@PathVariable("id") Long id){
        try {
            administratorService.removeAirlineCompany(id);
            return ResponseEntity.ok(new MessageResponse("The airline company and its user have been removed!"));
        }
        catch (AirlineCompanyNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/removeCustomer/{id}")
    //remove customer
    public ResponseEntity<?> removeCustomer(@PathVariable("id") Long id){
        try {
            administratorService.removeCustomer(id);
            return ResponseEntity.ok(new MessageResponse("The customer and its user have been removed!"));
        }
        catch (CustomerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/removeAdministrator/{id}")
    //remove administrator
    public ResponseEntity<?> removeAdministrator(@PathVariable("id") Integer id){
        try {
            administratorService.removeAdministrator(id);
            return ResponseEntity.ok(new MessageResponse("The administrator and its user have been removed!"));
        }
        catch (AdministratorNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @PostMapping("/addCustomerUser")
    //add customerUser
    public ResponseEntity<?> addCustomerUser(@RequestBody UserAndCustomerDto userAndCustomerDto){
        try{
            UserAndCustomerDto customerUser = administratorService.addCustomerUser(userAndCustomerDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(customerUser);
        }
        catch (ClientFaultException | DuplicateKeyException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @PostMapping("/addAdministratorUser")
    //add administratorUser
    public ResponseEntity<?> addAdministratorUser(@RequestBody UserAndAdministratorDto userAndAdministratorDto){
        try{
            UserAndAdministratorDto administratorUser = administratorService.addAdministratorUser(userAndAdministratorDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(administratorUser);
        }
        catch (ClientFaultException | DuplicateKeyException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @PostMapping("/addAirlineCompanyUser")
    //add airlineCompanyUser
    public ResponseEntity<?> addAirlineCompanyUser(@RequestBody UserAndAirlineCompanyDto userAndAirlineCompanyDto){
        try{
            UserAndAirlineCompanyDto airlineCompanyUser = administratorService.addAirlineCompanyUser(userAndAirlineCompanyDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(airlineCompanyUser);
        }
        catch (ClientFaultException | DuplicateKeyException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
}
