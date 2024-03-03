package com.example.project.service;

import com.example.project.model.*;
import com.example.project.model.payload.response.MessageResponse;
import com.example.project.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministratorService extends ServiceBase implements IAdministratorService{
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AirlineCompanyRepository airlineCompanyRepository;
    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private CacheRepositoryImpl cacheRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${cache_on}")
    private Boolean cache_on;
    private static final String customersKey = "customersList";
    private static final String administratorsKey = "administratorsList";


    @Override
    public List<Administrator> getAllAdministrators() {
        try {
            //check if the Key is exists
            if (cache_on && cacheRepository.isKeyExist(administratorsKey)) {
                System.out.println("is Key Exist? " + cacheRepository.isKeyExist(administratorsKey));
                String administrators = cacheRepository.getCacheEntity(administratorsKey);
                System.out.println("reading from administrators cache " + administrators);
                return objectMapper.readValue(administrators, new TypeReference<List<Administrator>>() {});
            }
            //if not..
            List<Administrator> allAdministrators = administratorRepository.getAllAdministrators();
            if(allAdministrators != null) {
                if (cache_on) {
                    cacheRepository.createCacheEntity(administratorsKey, objectMapper.writeValueAsString(allAdministrators));
                }
            }
            return allAdministrators;

        } catch (JsonProcessingException e) {
            System.out.println(e);
            throw new IllegalStateException(new MessageResponse("Cannot write JSON of all administrators: " + e.getMessage()).getMessage());
        }
    }


    @Override
    public Administrator getAdministratorById(Integer id) {
        return administratorRepository.getAdministratorById(id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        try {
            //check if the Key is exists
            if (cache_on && cacheRepository.isKeyExist(customersKey)) {
                System.out.println("is Key Exist? " + cacheRepository.isKeyExist(customersKey));
                String customers = cacheRepository.getCacheEntity(customersKey);
                System.out.println("reading from customers cache " + customers);
                return objectMapper.readValue(customers, new TypeReference<List<Customer>>() {});
            }
            //if not..
            List<Customer> allCustomers = customerRepository.getAllCustomers();
            if(allCustomers != null) {
                if (cache_on) {
                    cacheRepository.createCacheEntity(customersKey, objectMapper.writeValueAsString(allCustomers));
                }
            }
            return allCustomers;

        } catch (JsonProcessingException e) {
            System.out.println(e);
            throw new IllegalStateException(new MessageResponse("Cannot write JSON of all customers: " + e.getMessage()).getMessage());
        }
    }


    @Override
    public void updateAdministrator(Administrator administrator, Integer id) throws ClientFaultException {
        //verify if the administrator to be updated still exists
        Administrator existAdministrator = administratorRepository.getAdministratorById(id);
        if (existAdministrator != null) {
            //verify if the User ID has not changed
            Long userId = existAdministrator.getUser_id();
            if (userId.longValue() != administrator.getUser_id().longValue()) {
                throw new IllegalChangingUserIdException(new MessageResponse("Cannot change user id").getMessage());
            }
            administratorRepository.updateAdministrator(administrator, id);
        }
        else{
            throw new AdministratorNotFoundException(new MessageResponse("Administrator does not exist").getMessage());
        }
    }

    @Override
    public void removeAirlineCompany(Long id) throws ClientFaultException {
        //verify if the airline company to be removed still exists
        AirlineCompany existAirlineCompany = airlineCompanyRepository.getAirlineCompanyById(id);
        if(existAirlineCompany != null){
            User removeUser = userRepository.getUserById(existAirlineCompany.getUser_id());
            airlineCompanyRepository.removeAirlineCompany(id);
            //once the airline is removed, the user associated with that airline company should also be removed
            userRepository.removeUser(removeUser.getId());
        }
        else{
            throw new AirlineCompanyNotFoundException(new MessageResponse("Airline company does not exist").getMessage());
        }
    }


    @Override
    public void removeCustomer(Long id) throws ClientFaultException {
        //verify if the customer to be removed still exists
        Customer existCustomer = customerRepository.getCustomerById(id);
        if(existCustomer != null){
            User removeUser = userRepository.getUserById(existCustomer.getUser_id());
            customerRepository.removeCustomer(id);
            //once the customer is removed, the user associated with that customer should also be removed
            userRepository.removeUser(removeUser.getId());
        }
        else{
            throw new CustomerNotFoundException(new MessageResponse("Customer does not exist").getMessage());
        }
    }

    @Override
    public void removeAdministrator(Integer id) throws ClientFaultException {
        //verify if the administrator to be removed still exists
        Administrator existAdministrator = administratorRepository.getAdministratorById(id);
        if(existAdministrator != null){
            User removeUser = userRepository.getUserById(existAdministrator.getUser_id());
            administratorRepository.removeAdministrator(id);
            //once the administrator is removed, the user associated with that administrator should also be removed
            userRepository.removeUser(removeUser.getId());
        }
        else{
            throw new AdministratorNotFoundException(new MessageResponse("Administrator does not exist").getMessage());
        }
    }

    @Override
    public UserAndCustomerDto addCustomerUser(UserAndCustomerDto userAndCustomerDto) throws ClientFaultException {
        //getting the user, customer objects from the userAndCustomerDto
        User user = userAndCustomerDto.getUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Customer customer = userAndCustomerDto.getCustomer();

        UserRole userRole = userRoleRepository.getUserRoleByRoleName(UserRoleName.CUSTOMER);
        //set the ID of "Customer" Role
        user.setUser_role(userRole.getId());

        //create new user
        User newUser = userRepository.addUser(user);
        if(newUser != null) {
            //set the ID of the new user in the customer.user_id
            customer.setUser_id(newUser.getId());
            try {
                //create new customer
                Customer newCustomer = customerRepository.addCustomer(customer);
                return new UserAndCustomerDto().response(newUser, newCustomer);
            }catch (Exception e) {
                //if customer creation is unsuccessful, remove the newUser that created
                userRepository.removeUser(newUser.getId());
                throw new ClientFaultException(new MessageResponse("Failed to create a new customer with a newUser: " + e.getMessage()).getMessage());
            }
        }
        throw new ClientFaultException(new MessageResponse("Failed to create a new user").getMessage());
    }

    @Override
    public UserAndAdministratorDto addAdministratorUser(UserAndAdministratorDto userAndAdministratorDto) throws ClientFaultException {
        //getting the user, administrator objects from the userAndAdministratorDto
        User user = userAndAdministratorDto.getUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Administrator administrator = userAndAdministratorDto.getAdministrator();

        UserRole userRole = userRoleRepository.getUserRoleByRoleName(UserRoleName.ADMINISTRATOR);
        //set the ID of "Administrator" Role
        user.setUser_role(userRole.getId());

        //create new user
        User newUser = userRepository.addUser(user);
        if(newUser != null) {
            //set the ID of the new user in the administrator.user_id
            administrator.setUser_id(newUser.getId());
            try {
                //create new administrator
                Administrator newAdministrator = administratorRepository.addAdministrator(administrator);
                return new UserAndAdministratorDto().response(newUser, newAdministrator);
            }catch (Exception e) {
                //if administrator creation is unsuccessful, remove the newUser that created
                userRepository.removeUser(newUser.getId());
                throw new ClientFaultException(new MessageResponse("Failed to create a new administrator with a newUser: " + e.getMessage()).getMessage());
            }
        }
        throw new ClientFaultException(new MessageResponse("Failed to create a new user").getMessage());
    }

    @Override
    public UserAndAirlineCompanyDto addAirlineCompanyUser(UserAndAirlineCompanyDto userAndAirlineCompanyDto) throws ClientFaultException {
        //getting the user, airlineCompany objects from the userAndAirlineCompanyDto
        User user = userAndAirlineCompanyDto.getUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AirlineCompany airlineCompany = userAndAirlineCompanyDto.getAirlineCompany();

        UserRole userRole = userRoleRepository.getUserRoleByRoleName(UserRoleName.AIRLINE_COMPANY);
        //set the ID of "AirlineCompany" Role
        user.setUser_role(userRole.getId());

        //create new user
        User newUser = userRepository.addUser(user);
        if(newUser != null) {
            //set the ID of the new user in the airlineCompany.user_id
            airlineCompany.setUser_id(newUser.getId());
            try {
                //create new airlineCompany
                AirlineCompany newAirlineCompany = airlineCompanyRepository.addAirlineCompany(airlineCompany);
                return new UserAndAirlineCompanyDto().response(newUser, newAirlineCompany);
            }catch (Exception e) {
                //if airline creation is unsuccessful, remove the newUser that created
                userRepository.removeUser(newUser.getId());
                throw new ClientFaultException(new MessageResponse("Failed to create a new airline company with a newUser: " + e.getMessage()).getMessage());
            }
        }
        throw new ClientFaultException(new MessageResponse("Failed to create a new user").getMessage());
    }

}



