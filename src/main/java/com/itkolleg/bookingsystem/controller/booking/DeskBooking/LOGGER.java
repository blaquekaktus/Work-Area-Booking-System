package com.itkolleg.bookingsystem.controller.booking.DeskBooking;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.TimeSlot;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * A DTO for the {@link com.itkolleg.bookingsystem.domains.Booking.DeskBooking} entity
 */
@Data
public class LOGGER implements Serializable {
    private final Long id;
    @NotNull
    private final Employee employee;
    @NotNull
    private final Desk desk;
    @NotNull
    private final LocalDate date;
    private final TimeSlot timeSlot;
    private final LocalTime timeStamp;
    private final LocalTime start;
    private final LocalTime endTime;

}