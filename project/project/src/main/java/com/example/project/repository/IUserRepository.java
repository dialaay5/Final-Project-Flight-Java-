package com.example.project.repository;

import com.example.project.model.ClientFaultException;
import com.example.project.model.User;

import java.util.List;

public interface IUserRepository {
    User addUser(User user) throws ClientFaultException;
    void addAllUsers(List<User> users) throws ClientFaultException;
    void updateUser(User user, Long id);
    void removeUser(Long id);
    List<User> getAllUsers();
    User getUserById(Long id);
    User get_user_by_username(String user_name);
    User get_user_by_email(String email);

    }
