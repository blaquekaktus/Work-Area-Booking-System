package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Desk in the system.
 * Each desk has an identifier, a number of monitors, and a list of ports.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
@Entity
@Getter
@Setter
@ToString
public class Desk {

    private static final Logger logger = LoggerFactory.getLogger(Desk.class);

    /**
     * A list of ports available at the desk.
     */
    @NotEmpty(message = "Ports must not be empty")
    @Size(min = 1, message = "At least one port is required")
    @ElementCollection
    protected List<Port> ports;

    /**
     * The identifier for the desk.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * The desk number.
     */
    @NotEmpty(message = "Desk number must not be empty")
    private String deskNr;

    /**
     * The number of monitors at the desk.
     */
    @Digits(integer = 2, fraction = 0, message = "Number of monitors must be a positive integer (0 - 99)")
    @Min(value = 0, message = "Number of monitors can't be less than 0")
    @Max(value = 10, message = "Number of monitors can't exceed 10")
    private int nrOfMonitors;

    /**
     * The booking Status of the desk.
     */
    @NotNull
    private String bookingStatus = "NONE";

    /**
     * Default constructor that initializes the list of ports.
     */
    public Desk() {
        this.ports = new ArrayList<>();
    }

    /**
     * Constructor for creating a Desk with a specific desk number, number of monitors, a list of ports, and booking Status.
     */
    public Desk(@NonNull String deskNr, int nrOfMonitors, List<Port> ports) {
        this.deskNr = deskNr;
        this.nrOfMonitors = nrOfMonitors;
        this.ports = ports;
    }

    /**
     * Method to add a port to the list of ports for the desk.
     */
    public void addPort(Port port) {
        if (ports != null) this.ports.add(port);
    }

    /**
     * Method to delete all ports from the list of ports for the desk.
     */
    public void deleteAllPorts() {
        this.ports.clear();
    }
}
