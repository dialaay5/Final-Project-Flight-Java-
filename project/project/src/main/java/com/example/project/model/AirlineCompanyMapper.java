package com.example.project.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AirlineCompanyMapper implements RowMapper<AirlineCompany> {
    @Override
    public AirlineCompany mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AirlineCompany(
                rs.getLong("id"),
                rs.getString("airlineCompany_name"),
                rs.getInt("country_id"),
                rs.getLong("user_id")
        );
    }
}
