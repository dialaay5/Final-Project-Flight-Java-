package com.example.project.repository;

import com.example.project.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomerRepository implements ICustomerRepository {
    private static final String Customer_Table_Name = "customer";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    @Override
    public Customer addCustomer(Customer customer) throws ClientFaultException {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

            String queryNamedParam = String.format("INSERT INTO %s (customer_firstName, customer_lastName, address, phone_number, creditCard_number, user_id) VALUES (:customer_firstName, :customer_lastName, :address, :phone_number, :creditCard_number, :user_id)", Customer_Table_Name);

            Map<String, Object> params = new HashMap<>();
            params.put("customer_firstName", customer.getCustomer_firstName());
            params.put("customer_lastName", customer.getCustomer_lastName());
            params.put("address", customer.getAddress());
            params.put("phone_number", customer.getPhone_number());
            params.put("creditCard_number", customer.getCreditCard_number());
            params.put("user_id", customer.getUser_id());

            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(params);

            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

            namedParameterJdbcTemplate.update(queryNamedParam, mapSqlParameterSource, generatedKeyHolder);

            Long id = (Long) generatedKeyHolder.getKeys().get("id");

            customer.setId(id);
            return customer;

        } catch (DuplicateKeyException e) {
            throw new CreditCardOrPhoneNumberAlreadyExistsException("CreditCard/Phone number already exists");
        } catch (Exception e) {
            throw new UserIdNotFoundException("User not found");
        }
    }

    @Override
    public void addAllCustomers(List<Customer> customers) throws ClientFaultException {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            String queryNamedParam = String.format("INSERT INTO %s (customer_firstName, customer_lastName, address, phone_number, creditCard_number, user_id) VALUES (:customer_firstName, :customer_lastName, :address, :phone_number, :creditCard_number, :user_id)", Customer_Table_Name);

            List<MapSqlParameterSource> customersList = new ArrayList<>();
            for (Customer customer : customers) {
                MapSqlParameterSource customersParams = new MapSqlParameterSource();
                customersParams.addValue("customer_firstName", customer.getCustomer_firstName());
                customersParams.addValue("customer_lastName", customer.getCustomer_lastName());
                customersParams.addValue("address", customer.getAddress());
                customersParams.addValue("phone_number", customer.getPhone_number());
                customersParams.addValue("creditCard_number", customer.getCreditCard_number());
                customersParams.addValue("user_id", customer.getUser_id());
                customersList.add(customersParams);
            }
            namedParameterJdbcTemplate.batchUpdate(queryNamedParam, customersList.toArray(new MapSqlParameterSource[0]));
        }catch (DuplicateKeyException e) {
            throw new CreditCardOrPhoneNumberAlreadyExistsException("CreditCard/Phone number already exists");
        } catch (Exception e) {
            throw new UserIdNotFoundException("User not found");
        }
    }

    @Override
    public void updateCustomer(Customer customer, Long id) {
        String query = String.format("UPDATE %s SET customer_firstName=?, customer_lastName=?, address=?, phone_number=?, creditCard_number=?, user_id=? WHERE id= ?", Customer_Table_Name);
        jdbcTemplate.update(query, customer.getCustomer_firstName(), customer.getCustomer_lastName(),
                customer.getAddress(), customer.getPhone_number(), customer.getCreditCard_number(), customer.getUser_id(), id);
    }

    @Override
    public void removeCustomer(Long id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", Customer_Table_Name);
        jdbcTemplate.update(query, id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        String query = String.format("Select * from %s ORDER BY id ASC ", Customer_Table_Name);
        return jdbcTemplate.query(query, new CustomerMapper());
    }

    @Override
    public Customer getCustomerById(Long id) {
        String query = String.format("Select * from %s where id=?", Customer_Table_Name);
        try {
            return jdbcTemplate.queryForObject(query, new CustomerMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Customer get_customer_by_username(String user_name) {
        String query ="SELECT * FROM get_customer_by_username(?)";
        try {
            return jdbcTemplate.queryForObject(query, new CustomerMapper(), user_name);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
