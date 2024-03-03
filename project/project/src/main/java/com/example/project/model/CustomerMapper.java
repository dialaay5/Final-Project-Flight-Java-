package com.example.project.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper implements RowMapper<Customer> {
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Customer(
                rs.getLong("id"),
                rs.getString("customer_firstName"),
                rs.getString("customer_lastName"),
                rs.getString("address"),
                rs.getString("phone_number"),
                rs.getString("creditCard_number"),
                rs.getLong("user_id")
        );
    }
}
