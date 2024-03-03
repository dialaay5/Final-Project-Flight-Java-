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
public class AirlineCompanyRepository implements IAirlineCompanyRepository{
    private static final String AirlineCompany_Table_Name = "airlineCompany";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    @Override
    public AirlineCompany addAirlineCompany(AirlineCompany airlineCompany) throws ClientFaultException {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

            String queryNamedParam = String.format("INSERT INTO %s (airlineCompany_name, country_id, user_id) VALUES (:airlineCompany_name, :country_id, :user_id)", AirlineCompany_Table_Name);


            Map<String, Object> params = new HashMap<>();
            params.put("airlineCompany_name", airlineCompany.getAirlineCompany_name());
            params.put("country_id", airlineCompany.getCountry_id());
            params.put("user_id", airlineCompany.getUser_id());

            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(params);

            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

            namedParameterJdbcTemplate.update(queryNamedParam, mapSqlParameterSource, generatedKeyHolder);

            Long id = (Long) generatedKeyHolder.getKeys().get("id");

            airlineCompany.setId(id);
            return airlineCompany;

        } catch (DuplicateKeyException e) {
            throw new NamedAlreadyExistException("Airline Company name already exist");
        } catch (Exception e) {
            throw new UserIdNotFoundException("User not found");
        }
    }

    @Override
    public void addAllAirlineCompanies(List<AirlineCompany> airlineCompanies) throws ClientFaultException {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

            String queryNamedParam = String.format("INSERT INTO %s (airlineCompany_name, country_id, user_id) VALUES (:airlineCompany_name, :country_id, :user_id)", AirlineCompany_Table_Name);

            List<MapSqlParameterSource> airlineCompaniesList = new ArrayList<>();
            for (AirlineCompany airlineCompany : airlineCompanies) {
                MapSqlParameterSource airlineCompanyParams = new MapSqlParameterSource();
                airlineCompanyParams.addValue("airlineCompany_name", airlineCompany.getAirlineCompany_name());
                airlineCompanyParams.addValue("country_id", airlineCompany.getCountry_id());
                airlineCompanyParams.addValue("user_id", airlineCompany.getUser_id());
                airlineCompaniesList.add(airlineCompanyParams);
            }
            namedParameterJdbcTemplate.batchUpdate(queryNamedParam, airlineCompaniesList.toArray(new MapSqlParameterSource[0]));
        } catch (DuplicateKeyException e) {
            throw new NamedAlreadyExistException("Airline Company name already exist");
        } catch (Exception e) {
            throw new UserIdNotFoundException("User not found");
        }
    }

    @Override
    public void updateAirlineCompany(AirlineCompany airlineCompany, Long id) {
        String query = String.format("UPDATE %s SET airlineCompany_name=?, country_id=?, user_id=? WHERE id= ?", AirlineCompany_Table_Name);
        jdbcTemplate.update(query, airlineCompany.getAirlineCompany_name(), airlineCompany.getCountry_id(),
                airlineCompany.getUser_id(), id);
    }

    @Override
    public void removeAirlineCompany(Long id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", AirlineCompany_Table_Name);
        jdbcTemplate.update(query, id);
    }

    @Override
    public List<AirlineCompany> getAllAirlineCompanies() {
        String query = String.format("Select * from %s ORDER BY id ASC ", AirlineCompany_Table_Name);
        return jdbcTemplate.query(query, new AirlineCompanyMapper());
    }

    @Override
    public AirlineCompany getAirlineCompanyById(Long id) {
        String query = String.format("Select * from %s where id=?", AirlineCompany_Table_Name);
        try
        {
            return jdbcTemplate.queryForObject(query, new AirlineCompanyMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<AirlineCompany> getAirlinesByCountry( Integer country_id){
        String query = String.format("Select * from %s where country_id=?", AirlineCompany_Table_Name);
        try
        {
            return jdbcTemplate.query(query, new AirlineCompanyMapper(), country_id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public AirlineCompany get_airline_by_username(String user_name) {
        String query = "SELECT * FROM get_airline_by_username(?)";
        try{
        return jdbcTemplate.queryForObject(query, new AirlineCompanyMapper(), user_name);
    }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
