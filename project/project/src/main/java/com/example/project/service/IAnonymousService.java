package com.example.project.service;

import com.example.project.model.*;

public interface IAnonymousService {
    User login ( String user_name, String password )throws ClientFaultException;
    UserAndCustomerDto createNewCustomerUser(UserAndCustomerDto userAndCustomerDto) throws ClientFaultException;
    UserAndAdministratorDto createNewAdministratorUser(UserAndAdministratorDto userAndAdministratorDto) throws ClientFaultException;
    UserAndAirlineCompanyDto createNewAirlineCompanyUser(UserAndAirlineCompanyDto userAndAirlineCompanyDto) throws ClientFaultException;
    }
