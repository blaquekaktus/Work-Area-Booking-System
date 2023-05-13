package com.itkolleg.bookingsystem.domains.Booking;


import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Ressource;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
/*
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RessourceBooking extends Booking {
@ManyToOne
    private Ressource ressource;

    public RessourceBooking(Employee employee, Ressource ressource, LocalDateTime bookingStart, LocalDateTime bookingEnd, LocalDateTime bookingTime){
        super(employee, bookingStart, bookingEnd, bookingTime);
        this.ressource=ressource;

    }


}
 */