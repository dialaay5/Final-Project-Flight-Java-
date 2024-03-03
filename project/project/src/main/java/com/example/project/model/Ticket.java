package com.example.project.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class Ticket {
    protected Long id;
    protected Long flight_id;
    protected Long customer_id;

    public Ticket() {
    }

    public Ticket(Long id, Long flight_id, Long customer_id) {
        this.id = id;
        this.flight_id = flight_id;
        this.customer_id = customer_id;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", flight_id=" + flight_id +
                ", customer_id=" + customer_id +
                '}';
    }
}
