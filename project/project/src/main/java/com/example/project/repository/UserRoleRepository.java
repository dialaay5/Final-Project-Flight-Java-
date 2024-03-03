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
public class UserRoleRepository implements IUserRoleRepository {
    private static final String UserRole_Table_Name = "userRole";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    @Override
    public UserRole addUserRole(UserRole userRole) throws ClientFaultException {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

            String queryNamedParam = String.format("INSERT INTO %s (role_name) VALUES (:role_name)", UserRole_Table_Name);

            Map<String, Object> params = new HashMap<>();
            params.put("role_name", userRole.getRole_name().name());

            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(params);

            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

            namedParameterJdbcTemplate.update(queryNamedParam, mapSqlParameterSource, generatedKeyHolder);

            Integer id = (Integer) generatedKeyHolder.getKeys().get("id");

            userRole.setId(id);
            return userRole;

        } catch (Exception e) {
            throw new InvalidUserRoleException("Invalid user role");
        }
    }

    @Override
    public void addAllUserRoles(List<UserRole> userRoles) throws ClientFaultException {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            String queryNamedParam = String.format("INSERT INTO %s (role_name) VALUES (:role_name)", UserRole_Table_Name);

            List<MapSqlParameterSource> userRolesList = new ArrayList<>();
            for (UserRole userRole : userRoles) {
                MapSqlParameterSource userRolesParams = new MapSqlParameterSource();
                userRolesParams.addValue("role_name", userRole.getRole_name().name());
                userRolesList.add(userRolesParams);
            }
            namedParameterJdbcTemplate.batchUpdate(queryNamedParam, userRolesList.toArray(new MapSqlParameterSource[0]));

        } catch (Exception e) {
            throw new InvalidUserRoleException("Invalid user role");
        }
    }

    @Override
    public void updateUserRole(UserRole userRole, Integer id) {
        String query = String.format("UPDATE %s SET role_name=? WHERE id= ?", UserRole_Table_Name);
        jdbcTemplate.update(query, userRole.getRole_name().name(), id);
    }

    @Override
    public void removeUserRole(Integer id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", UserRole_Table_Name);
        jdbcTemplate.update(query, id);
    }

    @Override
    public List<UserRole> getAllUserRoles() {
        String query = String.format("Select * from %s ORDER BY id ASC ", UserRole_Table_Name);
        return jdbcTemplate.query(query, new UserRoleMapper());
    }

    @Override
    public UserRole getUserRoleById(Integer id) {
        String query = String.format("Select * from %s where id=?", UserRole_Table_Name);
        try {
            return jdbcTemplate.queryForObject(query, new UserRoleMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public UserRole getUserRoleByRoleName(UserRoleName role_name) {
        String query = String.format("Select * from %s where role_name =?", UserRole_Table_Name);
        try {
            return jdbcTemplate.queryForObject(query, new UserRoleMapper(), role_name.name());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}

