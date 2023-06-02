package com.itkolleg.bookingsystem.domains.Booking;


import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * This class extends the Booking class to include a Desk.
 * It represents a desk booking within the system.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class DeskBooking extends Booking {

    private static final Logger logger = LoggerFactory.getLogger(DeskBooking.class);

    /**
     * The desk associated with the booking.
     * It must not be null.
     */
    @NotNull(message = "Desk must not be null")
    @ToString.Include
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Desk desk;

    /**
     * Constructor for creating a desk booking with specific employee, desk, date, start and end times, and timestamp.
     */
    public DeskBooking(Employee employee, Desk desk, LocalDate date, LocalTime start, LocalTime endTime, LocalDateTime timeStamp) {
        super(employee, date, start, endTime, timeStamp);
        this.desk = desk;
    }
}