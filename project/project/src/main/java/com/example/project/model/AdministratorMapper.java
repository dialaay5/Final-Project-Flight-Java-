package com.example.project.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdministratorMapper implements RowMapper<Administrator> {
    @Override
    public Administrator mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Administrator(
                rs.getInt("id"),
                rs.getString("administrator_firstName"),
                rs.getString("administrator_lastName"),
                rs.getLong("user_id")
        );
    }
}
