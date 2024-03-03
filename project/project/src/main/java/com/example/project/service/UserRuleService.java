package com.example.project.service;

import com.example.project.model.*;
import com.example.project.model.payload.response.MessageResponse;
import com.example.project.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class UserRuleService implements IUserRuleService{
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Override
    public UserRole addUserRole(UserRole userRole) throws ClientFaultException {
        return userRoleRepository.addUserRole(userRole);
    }

    @Override
    public void addAllUserRoles(List<UserRole> userRoles) throws ClientFaultException {
        userRoleRepository.addAllUserRoles(userRoles);
    }

    @Override
    public void updateUserRole(UserRole userRole, Integer id) throws ClientFaultException {
        //verify if this is tha same userRole
        if (userRole.getId().equals(id)) {
            //verify if the userRole that requested update still exists.
            UserRole existUserRole = userRoleRepository.getUserRoleById(id);
            if (existUserRole != null) {
                userRoleRepository.updateUserRole(userRole, id);
            } else {
                throw new InvalidUserRoleException(new MessageResponse("UserRole not found").getMessage());
            }
        }
        else {
            throw new ClientFaultException(new MessageResponse("Provided a wrong data, cannot update another userRole").getMessage());
        }
    }

    @Override
    public void removeUserRole(Integer id) throws InvalidUserRoleException {
        //verify if the userRole that requested for removal still exists
        UserRole existUserRole = userRoleRepository.getUserRoleById(id);
        if (existUserRole != null) {
            userRoleRepository.removeUserRole(id);
        }
        else{
            throw new InvalidUserRoleException(new MessageResponse("UserRole not found").getMessage());
        }
    }

    @Override
    public List<UserRole> getAllUserRoles() {
        return userRoleRepository.getAllUserRoles();
    }

    @Override
    public UserRole getUserRoleById(Integer id) {
        return userRoleRepository.getUserRoleById(id);
    }

    @Override
    public UserRole getUserRoleByRoleName(UserRoleName role_name) {
        return userRoleRepository.getUserRoleByRoleName(role_name);
    }
}
