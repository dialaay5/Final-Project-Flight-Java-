package com.example.project.repository;

import com.example.project.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AdministratorRepository implements IAdministratorRepository{
    private static final String Administrator_Table_Name = "administrator";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    @Override
    public Administrator addAdministrator(Administrator administrator) throws ClientFaultException {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

            String queryNamedParam = String.format("INSERT INTO %s (administrator_firstName, administrator_lastName, user_id) VALUES (:administrator_firstName, :administrator_lastName, :user_id)", Administrator_Table_Name);

            Map<String, Object> params = new HashMap<>();
            params.put("administrator_firstName", administrator.getAdministrator_firstName());
            params.put("administrator_lastName", administrator.getAdministrator_lastName());
            params.put("user_id", administrator.getUser_id());

            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(params);

            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

            namedParameterJdbcTemplate.update(queryNamedParam, mapSqlParameterSource, generatedKeyHolder);

            Integer id = (Integer) generatedKeyHolder.getKeys().get("id");

            administrator.setId(id);
            return administrator;

        }  catch (Exception e) {
            throw new UserIdNotFoundException("User not found");
        }
    }

    @Override
    public void addAllAdministrator(List<Administrator> administrators) throws ClientFaultException {
        try{
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

            String queryNamedParam = String.format("INSERT INTO %s (administrator_firstName, administrator_lastName, user_id) VALUES (:administrator_firstName, :administrator_lastName, :user_id)", Administrator_Table_Name);

            List<MapSqlParameterSource> administratorsList = new ArrayList<>();
            for (Administrator administrator : administrators) {
                MapSqlParameterSource administratorParams = new MapSqlParameterSource();
                administratorParams.addValue("administrator_firstName", administrator.getAdministrator_firstName());
                administratorParams.addValue("administrator_lastName", administrator.getAdministrator_lastName());
                administratorParams.addValue("user_id", administrator.getUser_id());
                administratorsList.add(administratorParams);
            }
            namedParameterJdbcTemplate.batchUpdate(queryNamedParam, administratorsList.toArray(new MapSqlParameterSource[0]));
        }  catch (Exception e) {
        throw new UserIdNotFoundException("User not found");
    }
}

    @Override
    public void updateAdministrator(Administrator administrator, Integer id) {
        String query = String.format("UPDATE %s SET administrator_firstName=?, administrator_lastName=?, user_id=?  WHERE id= ?", Administrator_Table_Name);
        jdbcTemplate.update(query, administrator.getAdministrator_firstName(), administrator.getAdministrator_lastName(), administrator.getUser_id(), id);
    }

    @Override
    public void removeAdministrator(Integer id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", Administrator_Table_Name);
        jdbcTemplate.update(query, id);
    }

    @Override
    public List<Administrator> getAllAdministrators() {
        String query = String.format("Select * from %s ORDER BY id ASC ", Administrator_Table_Name);
        return jdbcTemplate.query(query, new AdministratorMapper());
    }

    @Override
    public Administrator getAdministratorById(Integer id) {
        String query = String.format("Select * from %s where id=?", Administrator_Table_Name);
        try
        {
            return jdbcTemplate.queryForObject(query, new AdministratorMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
