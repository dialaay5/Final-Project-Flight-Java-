package com.example.project.repository;

import com.example.project.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TicketRepository implements ITicketRepository{
    private static final String Ticket_Table_Name = "ticket";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    @Override
    public Ticket addTicket(Ticket ticket) throws ClientFaultException {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

            String queryNamedParam = String.format("INSERT INTO %s (flight_id, customer_id) VALUES (:flight_id, :customer_id)", Ticket_Table_Name);

            Map<String, Object> params = new HashMap<>();
            params.put("flight_id", ticket.getFlight_id());
            params.put("customer_id", ticket.getCustomer_id());

            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(params);

            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

            namedParameterJdbcTemplate.update(queryNamedParam, mapSqlParameterSource, generatedKeyHolder);

            Long id = (Long) generatedKeyHolder.getKeys().get("id");

            ticket.setId(id);
            return ticket;
        }  catch (DuplicateKeyException e) {
            throw new TicketPurchaseException("The customer has already bought a ticket, for this flight.");
        }  catch (Exception e) {
            throw new TicketsException("The CUSTOMER/Flight does not exists");
        }
    }

    @Override
    public void addAllTickets(List<Ticket> tickets) throws ClientFaultException {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            String queryNamedParam = String.format("INSERT INTO %s (flight_id, customer_id) VALUES (:flight_id, :customer_id)", Ticket_Table_Name);

            List<MapSqlParameterSource> ticketsList = new ArrayList<>();
            for (Ticket ticket : tickets) {
                MapSqlParameterSource ticketParams = new MapSqlParameterSource();
                ticketParams.addValue("flight_id", ticket.getFlight_id());
                ticketParams.addValue("customer_id", ticket.getCustomer_id());
                ticketsList.add(ticketParams);
            }
            namedParameterJdbcTemplate.batchUpdate(queryNamedParam, ticketsList.toArray(new MapSqlParameterSource[0]));
        } catch (DuplicateKeyException e) {
            throw new TicketPurchaseException("The customer has already bought a ticket, for this flight.");
        } catch (Exception e) {
            throw new TicketsException("The CUSTOMER/Flight does not exists");
        }
    }


    @Override
    public void updateTicket(Ticket ticket, Long id) {
        String query = String.format("UPDATE %s SET flight_id=?, customer_id=? WHERE id= ?", Ticket_Table_Name);
        jdbcTemplate.update(query, ticket.getFlight_id(), ticket.getCustomer_id(), id);
    }

    @Override
    public void removeTicket(Long id) {
        String query = String.format("DELETE FROM %s WHERE id= ?", Ticket_Table_Name);
        jdbcTemplate.update(query, id);
    }

    @Override
    public List<Ticket> getAllTickets() {
        String query = String.format("Select * from %s ORDER BY id ASC ", Ticket_Table_Name);
        return jdbcTemplate.query(query, new TicketsMapper());
    }

    @Override
    public Ticket getTicketById(Long id) {
        String query = String.format("Select * from %s where id=?", Ticket_Table_Name);
        try
        {
            return jdbcTemplate.queryForObject(query, new TicketsMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Ticket> get_tickets_by_customer(Long customer_id) {
        String query = "SELECT * FROM get_tickets_by_customer(?)";
        try {
            return jdbcTemplate.query(query, new TicketsMapper(), customer_id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
