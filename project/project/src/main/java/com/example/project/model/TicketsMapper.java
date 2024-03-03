package com.example.project.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketsMapper implements RowMapper<Ticket> {
    @Override
    public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Ticket(
                rs.getLong("id"),
                rs.getLong("flight_id"),
                rs.getLong("customer_id")

        );
    }
}
