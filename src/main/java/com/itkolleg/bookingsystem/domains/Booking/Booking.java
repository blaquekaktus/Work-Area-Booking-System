package com.itkolleg.bookingsystem.domains.Booking;

import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.TimeSlot;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.itkolleg.bookingsystem.domains.TimeSlot.ALL_DAY;
import static com.itkolleg.bookingsystem.domains.TimeSlot.PM;

/**
 * This class represents a Booking within the booking system.
 * It holds information about the booking's employee, date, time slot, timestamp, start and end times.
 * It also validates certain fields to ensure the integrity of the data.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public abstract class Booking {

    /**
     * Unique identifier for each booking.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * The employee associated with the booking.
     * It must not be null.
     */
    @NotNull(message = "Employee must not be null")
    @ManyToOne(fetch = FetchType.EAGER)
    private Employee employee;

    /**
     * The date for the booking.
     * It must be a present or future date.
     */
    @NotNull
    @FutureOrPresent(message = "The date must not be in the past")
    private LocalDate date;

    /**
     * The time slot for the booking.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    private TimeSlot timeSlot;

    /**
     * The timestamp of when the booking was made.
     * It must be a present or past date/time.
     */

    @CreatedDate
    @Column(updatable = false)
    @PastOrPresent(message = "The date created must not be in the future")
    protected LocalDateTime createdOn;


    @LastModifiedDate
    @Column(insertable = false)
    protected LocalDateTime updatedOn;

    /**
     * The start time of the booking.
     */
    private LocalTime start;

    /**
     * The end time of the booking.
     */
    private LocalTime endTime;


    /**
     * Constructor for creating a booking with specific employee, date, start and end times, and timestamp.
     */
    public Booking(@NotNull Employee employee, @NotNull LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.employee = employee;
        this.date = date;
        this.start = startTime;
        this.endTime = endTime;
    }

    /**
     * Constructor for creating a booking with specific employee, date, time slot, and timestamp.
     * The start and end times are determined by the time slot.
     */
    public Booking(@NotNull Employee employee, @NotNull LocalDate date, TimeSlot timeSlot) {
        this.employee = employee;
        this.date = date;
        this.timeSlot = timeSlot;

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

    /**
     * This method checks if the start time is before the end time.
     * It is used for data validation purposes.
     *
     * @return True if the start time is before the end time, false otherwise.
     */
    @AssertTrue(message = "Start time must be before end time")
    public boolean isValidTime() {
        return start.isBefore(endTime);
    }

    @PreUpdate
    protected void onUpdate() {
        updatedOn = LocalDateTime.now();
    }

    public LocalDateTime getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return this.updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
}