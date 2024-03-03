package com.example.project.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Flight {
    protected Long id;
    protected Long airlineCompany_id;
    protected Integer originCountry_id;
    protected Integer destinationCountry_id;
    protected LocalDateTime departure_time;
    protected LocalDateTime landing_time;
    protected  Integer remaining_tickets;

    public Flight() {
    }

    public Flight(Long id, Long airlineCompany_id, Integer originCountry_id, Integer destinationCountry_id, LocalDateTime departure_time, LocalDateTime landing_time, Integer remaining_tickets) {
        this.id = id;
        this.airlineCompany_id = airlineCompany_id;
        this.originCountry_id = originCountry_id;
        this.destinationCountry_id = destinationCountry_id;
        this.departure_time = departure_time;
        this.landing_time = landing_time;
        this.remaining_tickets = remaining_tickets;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", airlineCompany_id=" + airlineCompany_id +
                ", originCountry_id=" + originCountry_id +
                ", destinationCountry_id=" + destinationCountry_id +
                ", departure_time=" + departure_time +
                ", landing_time=" + landing_time +
                ", remaining_tickets=" + remaining_tickets +
                '}';
    }
}
