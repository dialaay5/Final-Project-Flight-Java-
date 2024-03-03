package com.example.project.controller;

import com.example.project.model.*;
import com.example.project.model.payload.request.LoginRequest;
import com.example.project.model.payload.response.JwtResponse;
import com.example.project.model.payload.response.MessageResponse;
import com.example.project.security.JwtUtils;
import com.example.project.service.AnonymousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AnonymousService anonymousService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            //authenticate user by using the username and password they provided during login
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUser_name(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //create a JWT token
            String jwt = jwtUtils.generateJwtToken(authentication);
            System.out.println("jwt: " + jwt);

            User user = anonymousService.login(loginRequest.getUser_name(), loginRequest.getPassword());
            System.out.println("user: "+ user);
            //if login successfully
            return ResponseEntity.ok(new JwtResponse(user,jwt));

        }
        catch (AuthenticationException | ClientFaultException e) {
            if (e instanceof BadCredentialsException) {
                return ResponseEntity.badRequest().body(new MessageResponse("Incorrect username or password!"));
            } else if (e instanceof DisabledException) {
                return ResponseEntity.badRequest().body(new MessageResponse("User is disabled!"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
            }
        }
    }

    @PostMapping("/signup/customer")
    public ResponseEntity<?> signupCustomerUser(@RequestBody UserAndCustomerDto userAndCustomerDto) {
        try {
            UserAndCustomerDto newCustomerUser = anonymousService.createNewCustomerUser(userAndCustomerDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCustomerUser);

        } catch (ClientFaultException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @PostMapping("/signup/administrator")
    public ResponseEntity<?> signupAdministratorUser(@RequestBody UserAndAdministratorDto userAndAdministratorDto) {
        try {
            UserAndAdministratorDto newAdministratorUser = anonymousService.createNewAdministratorUser(userAndAdministratorDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newAdministratorUser);

        } catch (ClientFaultException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    // body example:
    /*{
        "user": {
        "user_name": "user1",
                "password": "123456789",
                "email": "user1@gmail.com",
                "user_role": 0
    },
        "administrator": {
        "administrator_firstName": "diala",
                "administrator_lastName": "ayoub",
                "user_id": 0
    }
    }
     */

    @PostMapping("/signup/airline")
    public ResponseEntity<?> signupNewAirlineCompanyUser(@RequestBody UserAndAirlineCompanyDto userAndAirlineCompanyDto) {
        try {
            UserAndAirlineCompanyDto newAirlineCompanyUser = anonymousService.createNewAirlineCompanyUser(userAndAirlineCompanyDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newAirlineCompanyUser);

        } catch (ClientFaultException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    //the functions of serverBase
    @GetMapping("/getAllFlights")
    //get all flight
    public ResponseEntity<?> getAllFlight() {
        try {
            List<Flight> list = anonymousService.getAllFlights();
            return ResponseEntity.ok(list);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/getFlightById/{id}")
    //get flight by id
    public ResponseEntity<?> getFlightById(@PathVariable("id") Long id) {
        try {
            Flight flight = anonymousService.getFlightById(id);
            if (flight != null) {
                return ResponseEntity.ok(flight);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found flight with Id " + id));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/flightsByParameters/{originCountry_id}/{destinationCountry_id}/{departure_time}")
    //get flights by parameters
    //example "http://localhost:8087/api/auth/flightsByParameters/1/2/2023-08-01T12:00:00"
    public ResponseEntity<?> getFlightsByParameters(@PathVariable  Integer originCountry_id,@PathVariable Integer destinationCountry_id,@PathVariable LocalDateTime departure_time){
        try {
            List<Flight> flightsList = anonymousService.get_flights_by_parameters(originCountry_id, destinationCountry_id, departure_time);
            if (!flightsList.isEmpty()) {
                return ResponseEntity.ok(flightsList);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No flights found with this parameters " + originCountry_id + destinationCountry_id + departure_time));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/getAllAirlines")
    //get all airline companies
    public ResponseEntity<?> getAllAirlineCompanies() {
        try {
            List<AirlineCompany> list = anonymousService.getAllAirlineCompanies();
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
            AirlineCompany airlineCompany = anonymousService.getAirlineCompanyById(id);
            if (airlineCompany != null) {
                return ResponseEntity.ok(airlineCompany);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found airline company with Id " + id));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/getAllCountries")
    //get all countries
    public ResponseEntity<?> getAllCountries() {
        try {
            List<Country> list = anonymousService.getAllCountries();
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
            Country country = anonymousService.getCountryById(id);
            if (country != null) {
                return ResponseEntity.ok(country);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found country with Id " + id));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

}
