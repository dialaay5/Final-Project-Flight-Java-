package com.example.project.service;

import com.example.project.model.*;
import com.example.project.model.payload.response.MessageResponse;
import com.example.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerService extends ServiceBase implements ICustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void updateCustomer(Customer customer, Long id) throws ClientFaultException {
        //verify if this is the same connected customer user
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        //verify if the customer requesting an update still exists
        Customer existCustomer = customerRepository.getCustomerById(id);
        if (existCustomer != null) {
            String customerUsername = userRepository.getUserById(existCustomer.getUser_id()).getUser_name();
            if (connectedUsername.equals(customerUsername)) {
                //verify if the User ID has not changed
                Long userId = existCustomer.getUser_id();
                if (userId.longValue() != customer.getUser_id().longValue()) {
                    throw new IllegalChangingUserIdException(new MessageResponse("Cannot change user id").getMessage());
                }
                    customerRepository.updateCustomer(customer, id);
                }
            else {
                throw new ClientFaultException(new MessageResponse("It is impossible to update another customer, only the connected customer can update itself").getMessage());
            }
        }
        else {
            throw new CustomerNotFoundException(new MessageResponse("Customer does not exist").getMessage());
        }
    }

    @Override
    public Customer getCustomerById(Long id) throws ClientFaultException {
        //verify if this is the same connected customer user
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String customerUsername = userRepository.getUserById(customerRepository.getCustomerById(id).getUser_id()).getUser_name();
        if (connectedUsername.equals(customerUsername)) {
            return customerRepository.getCustomerById(id);
        } else {
            throw new ClientFaultException(new MessageResponse("It is impossible to get information about another customer, only the connected customer can access its own information").getMessage());
        }
    }



    @Override
    public Customer get_customer_by_username(String user_name) throws ClientFaultException {
        //verify if this is the same connected customer user
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String customerUsername = userRepository.get_user_by_username(user_name).getUser_name();
        if (connectedUsername.equals(customerUsername)) {
            return customerRepository.get_customer_by_username(user_name);
        } else {
            throw new ClientFaultException(new MessageResponse("It is impossible to get information about another customer, only the connected customer can access its own information").getMessage());
        }
    }

    private void verifyFlightAvailability(Long flightId) throws ClientFaultException {
        Flight flight = flightRepository.getFlightById(flightId);
        if (flight == null) {
            throw new FlightNotFoundException(new MessageResponse("Flight not found.").getMessage());
        }
        if (flight.getRemaining_tickets() == 0) {
            throw new UnavailableTicketsException(new MessageResponse("Sorry, there are no more tickets available for this flight").getMessage());
        }
        if (flight.getDeparture_time().isBefore(LocalDateTime.now())) {
            throw new FlightDateTimeException(new MessageResponse("Sorry, the flight date time has passed").getMessage());
        }
    }


    @Override
    public Ticket addTicket(Ticket ticket) throws ClientFaultException {
        //verify if this ticket belongs to the currently connected customer
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String customerUsername = userRepository.getUserById(customerRepository.getCustomerById(ticket.getCustomer_id()).getUser_id()).getUser_name();
        if (connectedUsername.equals(customerUsername)) {
           verifyFlightAvailability(ticket.getFlight_id());
            //buying ticket
            Ticket newTicket = ticketRepository.addTicket(ticket);
            //update the remaining tickets for a specific flight
            flightRepository.update_remaining_tickets_after_buying_ticket(ticket.getFlight_id());
            return newTicket;
        }
        else {
            throw new ClientFaultException(new MessageResponse("It is impossible to purchase another customer's ticket, only tickets belonging to the currently connected customer can be purchased").getMessage());
        }
    }

    @Override
    public void updateTicket(Ticket ticket, Long id) throws ClientFaultException {
        //verify if this ticket belongs to the currently connected customer
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String customerUsername = userRepository.getUserById(customerRepository.getCustomerById(ticket.getCustomer_id()).getUser_id()).getUser_name();

        //verify if the ticket that requesting an update still exists
        Ticket existTicket = ticketRepository.getTicketById(id);
        if (existTicket == null) {
            throw new TicketsException(new MessageResponse("Ticket not found").getMessage());
        }

        if (!connectedUsername.equals(customerUsername)) {
            throw new ClientFaultException(new MessageResponse("It is impossible to update another customer's ticket, only tickets belonging to the currently connected customer can be updated").getMessage());
        }

        //verify if flight ID is being changed
        Long flightIdBeforeUpdate = existTicket.getFlight_id();
        if (!flightIdBeforeUpdate.equals(ticket.getFlight_id())) {
           verifyFlightAvailability(ticket.getFlight_id());
            //update tickets and remaining tickets for both flights
            ticketRepository.updateTicket(ticket, id);
            flightRepository.update_remaining_tickets_after_returning_ticket(flightIdBeforeUpdate);
            flightRepository.update_remaining_tickets_after_buying_ticket(ticket.getFlight_id());
        } else {
            //update the ticket if flight ID remains the same
            ticketRepository.updateTicket(ticket, id);
        }
    }

    @Override
    public void removeTicket(Long id) throws ClientFaultException {
        //verify if this ticket belongs to the currently connected customer
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        //verify if the ticket requested for removal still exists
        Ticket existTicket = ticketRepository.getTicketById(id);
        if (existTicket == null) {
            throw new TicketsException(new MessageResponse("Ticket not found").getMessage());
        }
        String customerUsername = userRepository.getUserById(customerRepository.getCustomerById(existTicket.getCustomer_id()).getUser_id()).getUser_name();
        if (!connectedUsername.equals(customerUsername)) {
            throw new ClientFaultException(new MessageResponse("It is impossible to remove another customer's ticket, only tickets belonging to the currently connected customer can be removed").getMessage());
        }

        //get flight id
        Long ticketFlightId = existTicket.getFlight_id();

        //remove the customer ticket
        ticketRepository.removeTicket(id);

        //update the remaining tickets for a specific flight
        flightRepository.update_remaining_tickets_after_returning_ticket(ticketFlightId);
    }


    @Override
    public List<Ticket> get_tickets_by_customer(Long customer_id) throws ClientFaultException {
        //verify if this is the same connected customer user
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        //verify if the customer still exists
        Customer customer = customerRepository.getCustomerById(customer_id);
        if (customer != null) {
            String customerUsername = userRepository.getUserById(customer.getUser_id()).getUser_name();
            if (connectedUsername.equals(customerUsername)) {
                return ticketRepository.get_tickets_by_customer(customer.getId());
            } else {
                throw new ClientFaultException(new MessageResponse("It is impossible to get information about another customer, only the connected customer can access its own information").getMessage());
            }
        }
        else {
            throw new CustomerNotFoundException(new MessageResponse("Customer does not exist").getMessage());
        }
    }

    @Override
    public List<Flight> getFlightsByCustomer(Long customer_id) throws ClientFaultException {
        //verify if this is the same connected customer user
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        //verify if the customer still exists
        Customer customer = customerRepository.getCustomerById(customer_id);
        if (customer != null) {
            String customerUsername = userRepository.getUserById(customer.getUser_id()).getUser_name();
            if (connectedUsername.equals(customerUsername)) {
                return flightRepository.getFlightsByCustomer(customer.getId());
            } else {
                throw new ClientFaultException(new MessageResponse("It is impossible to get information about another customer, only the connected customer can access its own information").getMessage());
            }
        }
        else {
            throw new CustomerNotFoundException(new MessageResponse("Customer does not exist").getMessage());
        }
    }
    @Override
    public void removeCustomer(Long id) throws ClientFaultException {
        //verify if this is the same connected customer user
        String connectedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        //verify if the customer requested for removal still exists
        Customer existCustomer = customerRepository.getCustomerById(id);
        if (existCustomer != null) {
            String customerUsername = userRepository.getUserById(existCustomer.getUser_id()).getUser_name();
            if (connectedUsername.equals(customerUsername)) {
                User removeUser = userRepository.getUserById(existCustomer.getUser_id());
                customerRepository.removeCustomer(id);
                //once the customer is removed, the user associated with that customer should also be removed
                userRepository.removeUser(removeUser.getId());
            } else {
                throw new ClientFaultException(new MessageResponse("It is impossible to remove another customer, only the connected customer can remove itself").getMessage());
            }
        }
        else {
            throw new CustomerNotFoundException(new MessageResponse("Customer does not exist").getMessage());
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
                throw new ClientFaultException(new MessageResponse("failed to create a new customer with a newUser: " + e.getMessage()).getMessage());
            }
        }
        throw new ClientFaultException(new MessageResponse("failed to create a new user").getMessage());
    }
}



