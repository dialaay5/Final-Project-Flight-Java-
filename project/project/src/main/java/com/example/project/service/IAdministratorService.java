package com.example.project.service;

import com.example.project.model.*;

import java.util.List;

public interface IAdministratorService {
    List<Administrator> getAllAdministrators();
    Administrator getAdministratorById(Integer id);
    List<Customer> getAllCustomers();
    void updateAdministrator(Administrator administrator, Integer id) throws ClientFaultException;
    void removeAirlineCompany(Long id) throws ClientFaultException;
    void removeCustomer(Long id) throws ClientFaultException;
    void removeAdministrator(Integer id) throws ClientFaultException;
    UserAndCustomerDto addCustomerUser(UserAndCustomerDto userAndCustomerDto) throws ClientFaultException;
    UserAndAdministratorDto addAdministratorUser (UserAndAdministratorDto userAndAdministratorDto) throws ClientFaultException;
    UserAndAirlineCompanyDto addAirlineCompanyUser (UserAndAirlineCompanyDto userAndAirlineCompanyDto) throws ClientFaultException;

}
