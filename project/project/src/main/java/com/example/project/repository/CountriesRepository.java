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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CountriesRepository implements ICountriesRepository{
    private static final String Country_Table_Name = "country";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    @Override
    public Country addCountry(Country country) throws ClientFaultException {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

            String queryNamedParam = String.format("INSERT INTO %s (country_name) VALUES (:country_name)", Country_Table_Name);

            Map<String, Object> params = new HashMap<>();
            params.put("country_name", country.getCountry_name());

            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(params);

            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

            namedParameterJdbcTemplate.update(queryNamedParam, mapSqlParameterSource, generatedKeyHolder);

            Integer id = (Integer) generatedKeyHolder.getKeys().get("id");

            country.setId(id);
            return country;

        }  catch (Exception e) {
            throw new NamedAlreadyExistException("Country name already exist");
        }
    }

    @Override
    public List<Country> addAllCountries(List<Country> countries) throws ClientFaultException {
        List<Country> addedCountriesList = new ArrayList<>();

        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

            String queryNamedParam = String.format("INSERT INTO %s (country_name) VALUES (:country_name)", Country_Table_Name);

            for (Country country : countries) {
                MapSqlParameterSource countryParams = new MapSqlParameterSource("country_name", country.getCountry_name());

                GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
                namedParameterJdbcTemplate.update(queryNamedParam, countryParams, generatedKeyHolder);

                Integer id = (Integer) generatedKeyHolder.getKeys().get("id");
                country.setId(id);
                addedCountriesList.add(country);
            }

        } catch (Exception e) {
            throw new NamedAlreadyExistException("Some of the country names in the list already exists, Please verify this.The remaining country names in the list have been successfully added");
        }
        return addedCountriesList;
    }

    @Override
    public void updateCountry(Country country, Integer id) {
        String query = String.format("UPDATE %s SET country_name=? WHERE id= ?", Country_Table_Name);
        jdbcTemplate.update(query, country.getCountry_name(), id);
    }

    @Override
    public void removeCountry(Integer id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", Country_Table_Name);
        jdbcTemplate.update(query, id);
    }

    @Override
    public List<Country> getAllCountries() {
        String query = String.format("Select * from %s ORDER BY id ASC ", Country_Table_Name);
        return jdbcTemplate.query(query, new CountriesMapper());
    }

    @Override
    public Country getCountryById(Integer id) {
        String query = String.format("Select * from %s where id=?", Country_Table_Name);
        try
        {
            return jdbcTemplate.queryForObject(query, new CountriesMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
