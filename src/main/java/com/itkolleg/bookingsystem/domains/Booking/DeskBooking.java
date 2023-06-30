package com.itkolleg.bookingsystem.domains.Booking;


import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.TimeSlot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
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
     * @param employee The employee associated with the booking.
     * @param desk The desk associated with the booking.
     * @param date The date for the booking.
     * @param start The start time for the booking.
     * @param endTime The end time for the booking.
     */
    public DeskBooking(Employee employee, Desk desk, LocalDate date, LocalTime start, LocalTime endTime) {
        super(employee,
                date,
                start,
                endTime);
        this.desk = desk;
    }

    /**
     * Constructor for creating a desk booking with a specific employee, desk, date, and time slot.
     * @param employee The employee associated with the booking.
     * @param desk The desk associated with the booking.
     * @param date The date for the booking.
     * @param timeSlot The time slot for the booking.
     */
    public DeskBooking(Employee employee, Desk desk, LocalDate date, TimeSlot timeSlot) {
        super(employee,
                date,
                timeSlot);
        this.desk = desk;
    }

}