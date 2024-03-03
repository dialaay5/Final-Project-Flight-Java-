package com.example.project.repository;

import com.example.project.model.Administrator;
import com.example.project.model.ClientFaultException;

import java.util.List;

public interface IAdministratorRepository {
    Administrator addAdministrator(Administrator administrator) throws ClientFaultException;
    void addAllAdministrator(List<Administrator> administrators) throws ClientFaultException;
    void updateAdministrator(Administrator administrator, Integer id);

    void removeAdministrator(Integer id);

    List<Administrator> getAllAdministrators();

    Administrator getAdministratorById(Integer id);
}
