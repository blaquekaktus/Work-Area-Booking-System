package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a port in the system.
 * A port is an element that is used as a part of the desk setup.
 * Since Port is marked as @Embeddable, it will be included as a part of the Desk object in the database schema.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class Port {

    private static final Logger logger = LoggerFactory.getLogger(Port.class);


    /**
     * The name of the port.
     */
    @Size(min = 2, message = "The name of the port must be at least 2 characters long")
    private String name;

    /**
     * Constructor that sets up the name of the port.
     *
     * @param name The name of the port.
     */
    public Port(String name) {
        this.name = name;
    }

    /**
     * Overrides the toString() method to return the name of the port.
     *
     * @return The name of the port.
     */
    @Override
    public String toString() {
        return this.name;
    }
}
