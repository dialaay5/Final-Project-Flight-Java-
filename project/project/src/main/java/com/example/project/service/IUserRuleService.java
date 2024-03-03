package com.example.project.service;

import com.example.project.model.ClientFaultException;
import com.example.project.model.InvalidUserRoleException;
import com.example.project.model.UserRole;
import com.example.project.model.UserRoleName;

import java.util.List;

public interface IUserRuleService {
    UserRole addUserRole(UserRole userRole) throws ClientFaultException;
    void addAllUserRoles(List<UserRole> userRoles) throws ClientFaultException;
    void updateUserRole(UserRole userRole, Integer id) throws ClientFaultException;
    void removeUserRole(Integer id) throws InvalidUserRoleException;
    List<UserRole> getAllUserRoles();
    UserRole getUserRoleById(Integer id);
    UserRole getUserRoleByRoleName(UserRoleName role_name);

}
