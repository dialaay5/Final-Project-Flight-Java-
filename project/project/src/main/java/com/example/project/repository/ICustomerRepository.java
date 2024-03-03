package com.example.project.repository;

import com.example.project.model.ClientFaultException;
import com.example.project.model.Country;
import com.example.project.model.Customer;

import java.util.List;

public interface ICustomerRepository {
    Customer addCustomer(Customer customer) throws ClientFaultException;
    void addAllCustomers(List<Customer> customers) throws ClientFaultException;
    void updateCustomer(Customer customer, Long id);

    void removeCustomer(Long id);

    List<Customer> getAllCustomers();

    Customer getCustomerById(Long id);

    Customer get_customer_by_username(String user_name);
}
