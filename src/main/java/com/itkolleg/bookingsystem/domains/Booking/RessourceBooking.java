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
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RessourceBooking extends Booking {
    @ManyToOne
    private Ressource ressource;

    public RessourceBooking(Employee employee, Ressource ressource, LocalDate date, LocalTime startTime, LocalTime endTime) {
        super(employee, date, startTime, endTime);
        this.ressource = ressource;

    }
}


