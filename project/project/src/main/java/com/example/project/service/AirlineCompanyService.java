package com.example.project.service;

import com.example.project.model.*;
import com.example.project.model.payload.response.MessageResponse;
import com.example.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AirlineCompanyService extends ServiceBase implements IAirlineCompanyService{
    @Autowired
    private AirlineCompanyRepository airlineCompanyRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private CountriesRepository countriesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public List<Flight> get_flights_by_airline_id(Long airlineCompany_id) throws ClientFaultException {
        //verify if this is the same connected airline company user
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        //verify if the airline company requesting an update still exists
        AirlineCompany existAirlineCompany = airlineCompanyRepository.getAirlineCompanyById(airlineCompany_id);
        if (existAirlineCompany != null) {
            String airlineCompanyUsername = userRepository.getUserById(existAirlineCompany.getUser_id()).getUser_name();
            if (connectedUsername.equals(airlineCompanyUsername)) {
                return flightRepository.get_flights_by_airline_id(existAirlineCompany.getId());
            } else {
                throw new ClientFaultException(new MessageResponse("It is impossible to get information about another airline company, only the connected airline company can access its own information").getMessage());
            }
        }
        return null;
    }
    @Override
    public AirlineCompany get_airline_by_username(String user_name) throws ClientFaultException {
        //verify if this is the same connected airline company user
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String airlineCompanyUsername = userRepository.get_user_by_username(user_name).getUser_name();

        if (connectedUsername.equals(airlineCompanyUsername)) {
            return airlineCompanyRepository.get_airline_by_username(user_name);
        }
        else {
            throw new ClientFaultException(new MessageResponse("It is impossible to get information about another airline company, only the connected airline company can access its own information").getMessage());
        }
    }
    @Override
    public List<AirlineCompany> getAirlinesByCountry(Integer country_id){
        //verify if the country still exists
        Country country = countriesRepository.getCountryById(country_id);
        if (country != null) {
            return airlineCompanyRepository.getAirlinesByCountry(country.getId());
        }
        return null;
    }

    @Override
    public void updateAirlineCompany(AirlineCompany airlineCompany, Long id) throws ClientFaultException {
        //verify if this is the same connected airline company user
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        //verify if the airline company requesting an update still exists
        AirlineCompany existAirlineCompany = airlineCompanyRepository.getAirlineCompanyById(id);
        if (existAirlineCompany != null) {
            String airlineCompanyUsername = userRepository.getUserById(existAirlineCompany.getUser_id()).getUser_name();

            if (connectedUsername.equals(airlineCompanyUsername)) {
                //verify if the User ID has not changed
                Long userId = existAirlineCompany.getUser_id();
                if (userId.longValue() != airlineCompany.getUser_id().longValue()) {
                    throw new IllegalChangingUserIdException(new MessageResponse("Cannot change user id").getMessage());
                }
                //verify if the country ID has not changed
                Integer countryId = existAirlineCompany.getCountry_id();
                if (countryId.intValue() != airlineCompany.getCountry_id().intValue()) {
                    throw new ClientFaultException(new MessageResponse("Cannot change country id").getMessage());
                }
                airlineCompanyRepository.updateAirlineCompany(airlineCompany, id);
            }
            else {
                throw new ClientFaultException(new MessageResponse("It is impossible to update another airline company, only the connected airline company can update itself").getMessage());
            }
        }
        else {
            throw new AirlineCompanyNotFoundException(new MessageResponse("Airline company does not exist").getMessage());
        }
    }

    private void validateFlightData(Flight flight) throws IllegalDataException {
        //check if the provided data is accurate and valid
        if (flight.getRemaining_tickets() < 0 ||
                flight.getDeparture_time().isBefore(LocalDateTime.now()) ||
                flight.getDestinationCountry_id().intValue() == flight.getOriginCountry_id().intValue() ||
                flight.getLanding_time().isBefore(flight.getDeparture_time()) ||
                flight.getLanding_time().equals(flight.getDeparture_time())) {
            throw new IllegalDataException(new MessageResponse("The data you supplied is not valid").getMessage());
        }
    }

    @Override
    public void updateFlight(Flight flight, Long id) throws ClientFaultException {
        //verify if this flight belongs to the currently connected airline company
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String airlineCompanyUsername = userRepository.getUserById(airlineCompanyRepository.getAirlineCompanyById(flight.getAirlineCompany_id()).getUser_id()).getUser_name();

        //verify if the flight requesting an update still exists
        Flight existFlight = flightRepository.getFlightById(id);
        if (existFlight != null) {
            if (connectedUsername.equals(airlineCompanyUsername)) {
                //check validation
                validateFlightData(flight);
                flightRepository.updateFlight(flight, id);
            } else {
                throw new ClientFaultException(new MessageResponse("It is impossible to update another airline company's flight, only flights belonging to the currently connected airline company can be updated").getMessage());
            }
        } else {
            throw new FlightNotFoundException(new MessageResponse("Flight not found").getMessage());
        }
    }

    @Override
    public Flight addFlight(Flight flight) throws ClientFaultException {
        //verify if this flight belongs to the currently connected airline company
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String airlineCompanyUsername = userRepository.getUserById(airlineCompanyRepository.getAirlineCompanyById(flight.getAirlineCompany_id()).getUser_id()).getUser_name();

        if (connectedUsername.equals(airlineCompanyUsername)) {
            //check validation
            validateFlightData(flight);
            //add new flight
            return flightRepository.addFlight(flight);
        }
        else {
            throw new ClientFaultException(new MessageResponse("It is impossible to add another airline company's flight, only flights belonging to the currently connected airline company can be added").getMessage());
        }}

    @Override
    public void removeFlight(Long id) throws ClientFaultException {
        //verify if this flight belongs to the currently connected airline company
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        //verify if the flight requested for removal still exists
        Flight existFlight = flightRepository.getFlightById(id);
        if(existFlight != null) {
            String airlineCompanyUsername = userRepository.getUserById(airlineCompanyRepository.getAirlineCompanyById(existFlight.getAirlineCompany_id()).getUser_id()).getUser_name();
            if (connectedUsername.equals(airlineCompanyUsername)) {
                flightRepository.removeFlight(id);
            } else {
                throw new ClientFaultException(new MessageResponse("It is impossible to remove another airline company's flight, only flights belonging to the currently connected airline company can be removed").getMessage());
            }
        }
        else{
            throw new FlightNotFoundException(new MessageResponse("Flight not found").getMessage());
        }
    }

    @Override
    public UserAndAirlineCompanyDto addAirlineCompanyUser(UserAndAirlineCompanyDto userAndAirlineCompanyDto) throws ClientFaultException {
        //getting the user, airlineCompany objects from the userAndAirlineCompanyDto
        User user = userAndAirlineCompanyDto.getUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AirlineCompany airlineCompany = userAndAirlineCompanyDto.getAirlineCompany();

        UserRole userRole = userRoleRepository.getUserRoleByRoleName(UserRoleName.AIRLINE_COMPANY);
        //set the ID of "AirlineCompany" Role
        user.setUser_role(userRole.getId());

        //create new user
        User newUser = userRepository.addUser(user);
        if(newUser != null) {
            //set the ID of the new user in the airlineCompany.user_id
            airlineCompany.setUser_id(newUser.getId());
            try {
                //create new airlineCompany
                AirlineCompany newAirlineCompany = airlineCompanyRepository.addAirlineCompany(airlineCompany);
                return new UserAndAirlineCompanyDto().response(newUser, newAirlineCompany);
            }catch (Exception e) {
                //if airline creation is unsuccessful, remove the newUser that created
                userRepository.removeUser(newUser.getId());
                throw new ClientFaultException(new MessageResponse("Failed to create a new airline company with a newUser: " + e.getMessage()).getMessage());
            }
        }
        throw new ClientFaultException(new MessageResponse("Failed to create a new user").getMessage());
    }

    @Override
    public void removeAirlineCompany(Long id) throws ClientFaultException {
        //verify if this is the same connected airline company user
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        //verify if the airline company requested for removal still exists
        AirlineCompany existAirlineCompany = airlineCompanyRepository.getAirlineCompanyById(id);
        if (existAirlineCompany != null) {
            String airlineCompanyUsername = userRepository.getUserById(existAirlineCompany.getUser_id()).getUser_name();
            if (connectedUsername.equals(airlineCompanyUsername)) {
                User removeUser = userRepository.getUserById(existAirlineCompany.getUser_id());
                airlineCompanyRepository.removeAirlineCompany(id);
                //once the airline is removed, the user associated with that airline company should also be removed
                userRepository.removeUser(removeUser.getId());
            } else {
                throw new ClientFaultException(new MessageResponse("It is impossible to remove another airline company, only the connected airline company can remove itself").getMessage());
            }
        }
        else {
            throw new AirlineCompanyNotFoundException(new MessageResponse("Airline company does not exist").getMessage());
        }
    }
}





