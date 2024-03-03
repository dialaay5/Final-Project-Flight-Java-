package com.example.project.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FlightMapper implements RowMapper<Flight> {
    @Override
    public Flight mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Flight(
                rs.getLong("id"),
                rs.getLong("airlineCompany_id"),
                rs.getInt("originCountry_id"),
                rs.getInt("destinationCountry_id"),
                rs.getTimestamp("departure_time").toLocalDateTime(),
                rs.getTimestamp("landing_time").toLocalDateTime(),
                rs.getInt("remaining_tickets")
        );
    }
}
