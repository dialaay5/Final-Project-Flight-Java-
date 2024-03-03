package com.example.project.service;

import com.example.project.model.*;
import com.example.project.model.payload.response.MessageResponse;
import com.example.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AnonymousService extends ServiceBase implements IAnonymousService {
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
    private PasswordEncoder passwordEncoder;
    @Value("${minimumPasswordLen}")
    private Integer minimumPasswordLen;

    @Override
    public User login(String user_name, String password) throws ClientFaultException {
        //find the user by user_name
        User user = userRepository.get_user_by_username(user_name);
        if (user != null) {
            //check if the provided password matches the user's password
            if (passwordEncoder.matches(password, user.getPassword())) {
                //login was successful
                return user;
            }
            //matching was failed
            throw new InvalidPasswordException(new MessageResponse("Incorrect password!").getMessage());
        }
        //finding the user by user_name was failed
        throw new IncorrectUserNameException(new MessageResponse("Incorrect user name!").getMessage());
    }

    private void validateUserData(User user) throws ClientFaultException {
        //check if the provided user is already registered
        if (userRepository.get_user_by_username(user.getUser_name()) != null) {
            throw new UserNameOrEmailAlreadyExistsException(new MessageResponse("Username is already registered").getMessage());
        }
        //check if the provided password is valid
        if (user.getPassword().length() < minimumPasswordLen) {
            throw new InvalidPasswordException(new MessageResponse("Invalid password, Minimum password length is 8 characters").getMessage());
        }
        //check if the provided email is already registered
        if (userRepository.get_user_by_email(user.getEmail()) != null) {
            throw new UserNameOrEmailAlreadyExistsException(new MessageResponse("Email is already registered").getMessage());
        }
    }

    @Override
    public UserAndCustomerDto createNewCustomerUser(UserAndCustomerDto userAndCustomerDto) throws ClientFaultException {
        //check validation
        validateUserData(userAndCustomerDto.getUser());

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
                throw new ClientFaultException(new MessageResponse("failed to create a new customer with a newUser: " + e.getMessage()).getMessage());
            }
        }
        throw new ClientFaultException(new MessageResponse("failed to create a new user").getMessage());
    }
    @Override
    public UserAndAdministratorDto createNewAdministratorUser(UserAndAdministratorDto userAndAdministratorDto) throws ClientFaultException {
        //check validation
        validateUserData(userAndAdministratorDto.getUser());

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
                throw new ClientFaultException(new MessageResponse("failed to create a new administrator with a newUser: " + e.getMessage()).getMessage());
            }
        }
        throw new ClientFaultException(new MessageResponse("failed to create a new user").getMessage());
    }

    @Override
    public UserAndAirlineCompanyDto createNewAirlineCompanyUser(UserAndAirlineCompanyDto userAndAirlineCompanyDto) throws ClientFaultException {
        //check validation
        validateUserData(userAndAirlineCompanyDto.getUser());

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
                throw new ClientFaultException(new MessageResponse("failed to create a new airline company with a newUser: " + e.getMessage()).getMessage());
            }
        }
        throw new ClientFaultException(new MessageResponse("failed to create a new user").getMessage());
    }
}

