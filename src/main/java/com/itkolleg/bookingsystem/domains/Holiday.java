package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String description;
    private boolean isBookingAllowed;

    public Holiday() {}

    public Holiday(LocalDate date, String description, boolean isBookingAllowed) {
        this.date = date;
        this.description = description;
        this.isBookingAllowed = isBookingAllowed;
    }
}
