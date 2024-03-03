package com.example.project.service;

import com.example.project.model.*;
import com.example.project.model.payload.response.MessageResponse;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UsersService implements IUsersService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }
    @Override
    public User get_user_by_username(String user_name) throws ClientFaultException {
        //verify if this is the same connected user
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (connectedUsername.equals(user_name)) {
            return userRepository.get_user_by_username(user_name);
        }
        else {
            throw new ClientFaultException(new MessageResponse("It is impossible to get information about another user, only the connected user can access its own information").getMessage());
        }
    }

    @Override
    public User get_user_by_email(String email) throws ClientFaultException {
        //verify if this is the same connected user
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String userUsername = userRepository.get_user_by_email(email).getUser_name();
        if (connectedUsername.equals(userUsername)) {
            return userRepository.get_user_by_email(email);
        }
        else {
            throw new ClientFaultException(new MessageResponse("It is impossible to get information about another user, only the connected user can access its own information").getMessage());
        }
    }

    @Override
    public User addUser(User user) throws ClientFaultException {
        return userRepository.addUser(user);
    }

    @Override
    public void addAllUsers(List<User> users) throws ClientFaultException {
        userRepository.addAllUsers(users);
    }

    @Override
    public void removeUser(Long id) throws InvalidUserRoleException, UserIdNotFoundException {
        //verify if the user that requested removal still exists.
        User existUser = userRepository.getUserById(id);
        if (existUser != null) {
            userRepository.removeUser(id);
        }
        else{
            throw new UserIdNotFoundException(new MessageResponse("User not found").getMessage());
        }
    }

    @Override
    public void updateUser(User user, Long id) throws ClientFaultException {
        //verify if this is the same connected user
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String userUsername = user.getUser_name();

        //verify if the user requesting an update exist
        User existUser = userRepository.getUserById(id);
        if (existUser != null) {
            if (connectedUsername.equals(userUsername)) {
                //verify if the UserRole has not changed
                Integer userrole = existUser.getUser_role();
                if (userrole.intValue() != user.getUser_role().intValue()) {
                    throw new IllegalChangingUserIdException(new MessageResponse("Cannot change user role").getMessage());
                }
                userRepository.updateUser(user, id);
            } else {
                throw new ClientFaultException(new MessageResponse("You do not have permission to update other user").getMessage());
            }
        } else {
            throw new UserIdNotFoundException(new MessageResponse("User not found").getMessage());
        }
    }
}
