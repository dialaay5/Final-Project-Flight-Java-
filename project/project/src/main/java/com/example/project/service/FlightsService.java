package com.example.project.service;

import com.example.project.model.*;
import com.example.project.model.payload.response.MessageResponse;
import com.example.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightsService implements IFlightsService {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private CountriesRepository countriesRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AirlineCompanyRepository airlineCompanyRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Flight> getAllFlights() {
        return flightRepository.getAllFlights();
    }

    @Override
    public Flight getFlightById(Long id) throws ClientFaultException {
        return flightRepository.getFlightById(id);
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
        //verify if this is tha same flight
        if (flight.getId().equals(id)) {
            //verify if the flight requesting an update still exists
            Flight existFlight = flightRepository.getFlightById(id);
            if (existFlight != null) {
                //check validation
                validateFlightData(flight);
                flightRepository.updateFlight(flight, id);
            } else {
                throw new FlightNotFoundException(new MessageResponse("Flight not found").getMessage());
            }
        }
        else {
            throw new ClientFaultException("provided a wrong data");
        }
    }

    @Override
    public Flight addFlight(Flight flight) throws ClientFaultException {
        validateFlightData(flight);
        return flightRepository.addFlight(flight);
    }

    @Override
    public void removeFlight(Long id) throws ClientFaultException {
        //verify if the flight that requested removal still exists.
        Flight existFlight = flightRepository.getFlightById(id);
        if (existFlight != null) {
            flightRepository.removeFlight(id);
        }else {
            throw new FlightNotFoundException(new MessageResponse("Flight not found").getMessage());
        }
    }

    @Override
    public List<Flight> getFlightsByOriginCountryId(Integer originCountry_id) {
        //verify if the originCountry still exists
        Country country = countriesRepository.getCountryById(originCountry_id);
        if (country != null) {
            return flightRepository.getFlightsByOriginCountryId(country.getId());
        }
        return null;
    }

    @Override
    public List<Flight> getFlightsByDestinationCountryId(Integer destinationCountry_id) {
        //verify if the destinationCountry still exists
        Country country = countriesRepository.getCountryById(destinationCountry_id);
        if (country != null) {
            return flightRepository.getFlightsByDestinationCountryId(country.getId());
        }
        return null;
    }


    @Override
    public List<Flight> getFlightsByDepartureDate(LocalDateTime departure_time) {
        return flightRepository.getFlightsByDepartureDate(departure_time);
    }

    @Override
    public List<Flight> getFlightsByLandingDate(LocalDateTime landing_time) {
        return flightRepository.getFlightsByLandingDate(landing_time);
    }
    @Override
    public List<Flight> get_flights_by_parameters(Integer originCountry_id, Integer destinationCountry_id, LocalDateTime departure_time) {
        return flightRepository.get_flights_by_parameters(originCountry_id, destinationCountry_id, departure_time);
    }

    @Override
    public List<Flight> get_arrival_flights(Integer destinationCountry_id) {
        //verify if the destinationCountry still exists
        Country country = countriesRepository.getCountryById(destinationCountry_id);
        if (country != null) {
            return flightRepository.get_arrival_flights(country.getId());
        }
        return null;
    }

    @Override
    public List<Flight> get_departure_flights(Integer originCountry_id) {
        //verify if the originCountry still exists
        Country country = countriesRepository.getCountryById(originCountry_id);
        if (country != null) {
            return flightRepository.get_departure_flights(country.getId());
        }
        return null;
    }

    @Override
    public void addAllFlights(List<Flight> flights) throws ClientFaultException {
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        for (Flight flight : flights) {
            //verify if this flight belongs to the currently connected airline company
            String airlineCompanyUsername = userRepository.getUserById(airlineCompanyRepository.getAirlineCompanyById(flight.getAirlineCompany_id()).getUser_id()).getUser_name();
            if (!connectedUsername.equals(airlineCompanyUsername)) {
                throw new ClientFaultException("You do not have permission to add flights to other airline companies' flights.");
            }
            // Check validation
            validateFlightData(flight);
        }
        //add the list of flights
        flightRepository.addAllFlights(flights);
    }
}





