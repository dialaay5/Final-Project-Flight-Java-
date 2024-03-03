package com.example.project.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountriesMapper implements RowMapper<Country> {
    @Override
    public Country mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Country(
                rs.getInt("id"),
                rs.getString("country_name")
        );
    }
}