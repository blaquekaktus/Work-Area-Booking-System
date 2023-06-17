package com.itkolleg.bookingsystem.domains;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents error details to be shown in case of exceptions or validation failures.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
public class ErrorDetails {

    private static final Logger logger = LoggerFactory.getLogger(ErrorDetails.class);

    /**
     * The title of the error.
     */
    @NotEmpty(message = "Error title must not be empty")
    @Getter
    @Setter
    private String title;

    /**
     * The detail message of the error.
     */
    @NotEmpty(message = "Error message must not be empty")
    @Getter
    @Setter
    private String message;

    /**
     * Default constructor.
     */
    public ErrorDetails() {
    }

    /**
     * Constructor that sets up the title and message for the error.
     *
     * @param title   The title of the error.
     * @param message The detailed message of the error.
     */
    public ErrorDetails(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
