package com.example.project.repository;

import com.example.project.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FlightRepository implements IFlightRepository{
    private static final String Flight_Table_Name = "flight";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    @Override
    public Flight addFlight(Flight flight) throws ClientFaultException {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

            String queryNamedParam = String.format("INSERT INTO %s (airlineCompany_id, originCountry_id, destinationCountry_id, departure_time, landing_time, remaining_tickets)" +
                    " VALUES (:airlineCompany_id, :originCountry_id, :destinationCountry_id, :departure_time, :landing_time, :remaining_tickets)", Flight_Table_Name);

            Map<String, Object> params = new HashMap<>();
            params.put("airlineCompany_id", flight.getAirlineCompany_id());
            params.put("originCountry_id", flight.getOriginCountry_id());
            params.put("destinationCountry_id", flight.getDestinationCountry_id());
            params.put("departure_time", flight.getDeparture_time());
            params.put("landing_time", flight.getLanding_time());
            params.put("remaining_tickets", flight.getRemaining_tickets());

            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(params);

            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

            namedParameterJdbcTemplate.update(queryNamedParam, mapSqlParameterSource, generatedKeyHolder);

            Long id = (Long) generatedKeyHolder.getKeys().get("id");

            flight.setId(id);
            return flight;

        }  catch (Exception e) {
            throw new AirlineCompanyNotFoundException("Airline company not found");
        }
    }

    @Override
    public void addAllFlights(List<Flight> flights) throws ClientFaultException {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            String queryNamedParam = String.format("INSERT INTO %s (airlineCompany_id, originCountry_id, destinationCountry_id, departure_time, landing_time, remaining_tickets)" +
                    " VALUES (:airlineCompany_id, :originCountry_id, :destinationCountry_id, :departure_time, :landing_time, :remaining_tickets)", Flight_Table_Name);

            List<MapSqlParameterSource> flightsList = new ArrayList<>();
            for (Flight flight : flights) {
                MapSqlParameterSource flightsParams = new MapSqlParameterSource();
                flightsParams.addValue("airlineCompany_id", flight.getAirlineCompany_id());
                flightsParams.addValue("originCountry_id", flight.getOriginCountry_id());
                flightsParams.addValue("destinationCountry_id", flight.getDestinationCountry_id());
                flightsParams.addValue("departure_time", flight.getDeparture_time());
                flightsParams.addValue("landing_time", flight.getLanding_time());
                flightsParams.addValue("remaining_tickets", flight.getRemaining_tickets());
                flightsList.add(flightsParams);
            }
            namedParameterJdbcTemplate.batchUpdate(queryNamedParam, flightsList.toArray(new MapSqlParameterSource[0]));
        } catch (Exception e) {
            throw new AirlineCompanyNotFoundException("Airline company not found");
        }
    }

    @Override
    public void updateFlight(Flight flight, Long id) {
        String query = String.format("UPDATE %s SET airlineCompany_id=?, originCountry_id=?, destinationCountry_id=?, departure_time=?, landing_time=?, remaining_tickets=?  WHERE id= ?", Flight_Table_Name);
        jdbcTemplate.update(query, flight.getAirlineCompany_id(), flight.getOriginCountry_id(),
                flight.getDestinationCountry_id(), flight.getDeparture_time(), flight.getLanding_time(),flight.getRemaining_tickets(), id);
    }

    @Override
    public void removeFlight(Long id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", Flight_Table_Name);
        jdbcTemplate.update(query, id);
    }

    @Override
    public List<Flight> getAllFlights() {
        String query = String.format("Select * from %s ORDER BY id ASC ", Flight_Table_Name);
        return jdbcTemplate.query(query, new FlightMapper());
    }

    @Override
    public Flight getFlightById(Long id) {
        String query = String.format("Select * from %s where id=?", Flight_Table_Name);
        try
        {
            return jdbcTemplate.queryForObject(query, new FlightMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Flight> getFlightsByOriginCountryId(Integer originCountry_id){
        String query = String.format("Select * from %s where originCountry_id=?", Flight_Table_Name);
        try
        {
            return jdbcTemplate.query(query, new FlightMapper(), originCountry_id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Flight> getFlightsByDestinationCountryId(Integer destinationCountry_id){
        String query = String.format("Select * from %s where destinationCountry_id=?", Flight_Table_Name);
        try
        {
            return jdbcTemplate.query(query, new FlightMapper(), destinationCountry_id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Flight> getFlightsByDepartureDate(LocalDateTime departure_time){
        String query = String.format("Select * from %s where departure_time =?", Flight_Table_Name);
        try
        {
            return jdbcTemplate.query(query, new FlightMapper(), departure_time);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Flight> getFlightsByLandingDate(LocalDateTime landing_time){
        String query = String.format("Select * from %s where landing_time =?", Flight_Table_Name);
        try
        {
            return jdbcTemplate.query(query, new FlightMapper(), landing_time);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Flight> getFlightsByCustomer(Long customer_id){
        String query = String.format("Select f.* from %s f JOIN ticket t ON f.id = t.flight_id WHERE t.customer_id = ?", Flight_Table_Name);
        try
        {
            return jdbcTemplate.query(query, new FlightMapper(), customer_id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Flight> get_flights_by_parameters(Integer originCountry_id,Integer destinationCountry_id,LocalDateTime departure_time) {
        String query = "SELECT * FROM get_flights_by_parameters(?,?,?)";
        try {
            return jdbcTemplate.query(query, new FlightMapper(), originCountry_id,destinationCountry_id,departure_time);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Override
    public List<Flight> get_flights_by_airline_id(Long airlineCompany_id) {
        String query = "SELECT * FROM get_flights_by_airline_id(?)";
        try {
            return jdbcTemplate.query(query, new FlightMapper(), airlineCompany_id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Flight> get_arrival_flights(Integer destinationCountry_id) {
        String query = "SELECT * FROM get_arrival_flights(?)";
        try {
            return jdbcTemplate.query(query, new FlightMapper(), destinationCountry_id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Flight> get_departure_flights(Integer originCountry_id) {
        String query = "SELECT * FROM get_departure_flights(?)";
        try {
            return jdbcTemplate.query(query, new FlightMapper(), originCountry_id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    //when a customer purchases a ticket for a specific flight, the remaining_tickets count should be updated
    @Override
    public void update_remaining_tickets_after_buying_ticket(Long id) {
        String query = String.format("UPDATE %s SET remaining_tickets = remaining_tickets - 1 WHERE id = ?", Flight_Table_Name);
        jdbcTemplate.update(query, id);
    }

    //when a customer removed a ticket for a specific flight, the remaining_tickets count should be updated
    @Override
    public void update_remaining_tickets_after_returning_ticket(Long id){
        String query = String.format("UPDATE %s SET remaining_tickets = remaining_tickets + 1 WHERE id = ?", Flight_Table_Name);
        jdbcTemplate.update(query, id);
    }
}
