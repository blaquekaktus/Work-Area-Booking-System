package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

/**
 * This class represents public holidays in the booking system.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
@Entity
@Getter
@Setter
public class PublicHoliday {

    private static final Logger logger = LoggerFactory.getLogger(PublicHoliday.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The date of the public holiday.
     */
    @FutureOrPresent(message = "The date of the public holiday must not be in the past")
    private LocalDate date;

    /**
     * The description of the public holiday.
     */
    @NotEmpty(message = "The description of the public holiday must not be empty")
    private String description;

    /**
     * Specifies whether booking is allowed on this public holiday or not.
     */
    private boolean isBookingAllowed;

    /**
     * Default constructor.
     */
    public PublicHoliday() {
    }

    /**
     * Constructor that sets up the date, description, and booking allowance for the public holiday.
     *
     * @param date             The date of the public holiday.
     * @param description      The description of the public holiday.
     * @param isBookingAllowed Specifies whether booking is allowed on this public holiday or not.
     */
    public PublicHoliday(LocalDate date, String description, boolean isBookingAllowed) {
        this.date = date;
        this.description = description;
        this.isBookingAllowed = isBookingAllowed;
    }
}
