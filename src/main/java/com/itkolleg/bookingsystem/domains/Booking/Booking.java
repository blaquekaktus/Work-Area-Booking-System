package com.itkolleg.bookingsystem.domains.Booking;

import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.TimeSlot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.itkolleg.bookingsystem.domains.TimeSlot.ALL_DAY;
import static com.itkolleg.bookingsystem.domains.TimeSlot.PM;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Employee employee;
    @NotNull
    private LocalDate date;
    @ManyToOne(fetch = FetchType.EAGER)
    private TimeSlot timeSlot;
    private LocalDateTime timeStamp;
    private LocalTime start;
    private LocalTime endTime;

    public Booking(@NotNull Employee employee, @NotNull LocalDate date, LocalTime startTime, LocalTime endTime, @NotNull LocalDateTime timeStamp) {
        this.employee = employee;
        this.date = date;
        this.start = startTime;
        this.endTime = endTime;
        this.timeStamp = timeStamp;
    }

    public Booking(@NotNull Employee employee, @NotNull LocalDate date, TimeSlot timeSlot, @NotNull LocalDateTime timeStamp) {
        this.employee = employee;
        this.date = date;
        this.timeSlot = timeSlot;
        this.timeStamp = timeStamp;

        if (timeSlot.equals(TimeSlot.AM)) {
            this.start = LocalTime.of(8, 0);
            this.endTime = LocalTime.of(12, 30);
        } else if (timeSlot.equals(PM)) {
            this.start = LocalTime.of(12, 30);
            this.endTime = LocalTime.of(17, 0);
        } else if (timeSlot.equals(ALL_DAY)) {
            this.start = LocalTime.of(8, 0);
            this.endTime = LocalTime.of(17, 0);
        } else {
            throw new IllegalArgumentException("Invalid time slot");
        }
    }
}