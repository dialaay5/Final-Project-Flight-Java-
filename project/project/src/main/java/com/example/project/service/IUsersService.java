package com.example.project.service;

import com.example.project.model.ClientFaultException;
import com.example.project.model.InvalidUserRoleException;
import com.example.project.model.User;
import com.example.project.model.UserIdNotFoundException;

import java.util.List;

public interface IUsersService {
    void updateUser(User user, Long id) throws ClientFaultException;
    List<User> getAllUsers();
    User getUserById(Long id);
    User get_user_by_username(String user_name) throws ClientFaultException;
    User get_user_by_email(String email) throws ClientFaultException;
    User addUser(User user) throws ClientFaultException;
    void addAllUsers(List<User> users) throws ClientFaultException;
    void removeUser(Long id) throws InvalidUserRoleException, UserIdNotFoundException;
}
