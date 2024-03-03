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
public class UserRepository implements IUserRepository{
    private static final String User_Table_Name = "users";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    @Override
    public User addUser(User user) throws ClientFaultException {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

            String queryNamedParam = String.format("INSERT INTO %s (user_name, password, email, user_role) VALUES (:user_name, :password, :email, :user_role)", User_Table_Name);

            Map<String, Object> params = new HashMap<>();
            params.put("user_name", user.getUser_name());
            params.put("password", user.getPassword());
            params.put("email", user.getEmail());
            params.put("user_role", user.getUser_role());

            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(params);

            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

            namedParameterJdbcTemplate.update(queryNamedParam, mapSqlParameterSource, generatedKeyHolder);

            Long id = (Long) generatedKeyHolder.getKeys().get("id");

            user.setId(id);
            return user;

        }  catch (DuplicateKeyException e) {
            throw new UserNameOrEmailAlreadyExistsException("UserName/Email already exists");
        } catch (Exception e) {
            throw new InvalidUserRoleException("Sorry, The user role you provided is invalid");
        }
    }
    @Override
    public void addAllUsers(List<User> users) throws ClientFaultException {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            String queryNamedParam = String.format("INSERT INTO %s (user_name, password, email, user_role) VALUES (:user_name, :password, :email, :user_role)", User_Table_Name);

            List<MapSqlParameterSource> usersList = new ArrayList<>();
            for (User user : users) {
                MapSqlParameterSource usersParams = new MapSqlParameterSource();
                usersParams.addValue("user_name", user.getUser_name());
                usersParams.addValue("password", user.getPassword());
                usersParams.addValue("email", user.getEmail());
                usersParams.addValue("user_role", user.getUser_role());
                usersList.add(usersParams);
            }
            namedParameterJdbcTemplate.batchUpdate(queryNamedParam, usersList.toArray(new MapSqlParameterSource[0]));

        }  catch (DuplicateKeyException e) {
            throw new UserNameOrEmailAlreadyExistsException("UserName/Email already exists");
        } catch (Exception e) {
            throw new InvalidUserRoleException("Invalid user role.Please provide a correct user role");
        }
    }

    @Override
    public void updateUser(User user, Long id) {
        String query = String.format("UPDATE %s SET user_name=?, password=?, email=?, user_role=? WHERE id= ?", User_Table_Name);
        jdbcTemplate.update(query, user.getUser_name(), user.getPassword(),
                user.getEmail(), user.getUser_role(), id);
    }

    @Override
    public void removeUser(Long id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", User_Table_Name);
        jdbcTemplate.update(query, id);
    }

    @Override
    public List<User> getAllUsers() {
        String query = String.format("Select * from %s ORDER BY id ASC ", User_Table_Name);
        return jdbcTemplate.query(query, new UserMapper());
    }

    @Override
    public User getUserById(Long id) {
        String query = String.format("Select * from %s where id=?", User_Table_Name);
        try
        {
            return jdbcTemplate.queryForObject(query, new UserMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User get_user_by_username(String user_name) {
        String query = "SELECT * FROM get_user_by_username(?)";
        try {
            return jdbcTemplate.queryForObject(query, new UserMapper(), user_name);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User get_user_by_email(String email) {
        String query = "SELECT * FROM get_user_by_email(?)";
        try {
            return jdbcTemplate.queryForObject(query, new UserMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
