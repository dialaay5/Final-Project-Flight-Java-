package com.example.project.repository;

import com.example.project.model.ClientFaultException;
import com.example.project.model.Ticket;
import com.example.project.model.UserRole;
import com.example.project.model.UserRoleName;

import java.util.List;

public interface IUserRoleRepository {
    UserRole addUserRole(UserRole userRole) throws ClientFaultException;
    void addAllUserRoles(List<UserRole> userRoles) throws ClientFaultException;
    void updateUserRole(UserRole userRole, Integer id);
    void removeUserRole(Integer id);
    List<UserRole> getAllUserRoles();

    UserRole getUserRoleById(Integer id);
    UserRole getUserRoleByRoleName(UserRoleName role_name);
}
