package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString

public class Desk {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotEmpty
    private String deskNr;

    @Digits(integer = 2, fraction = 0)
    @Min(0)
    @Max(10)
    private int nrOfMonitors;

    @NotEmpty
    @Size(min = 1)
    @ElementCollection
    protected List<Port> ports;

    public Desk() {
        this.ports = new ArrayList<>();
    }

    public Desk(@NonNull String deskNr, int nrOfMonitors, List<Port> ports) {
        this.deskNr = deskNr;
        this.nrOfMonitors = nrOfMonitors;
        this.ports = ports;
    }

    public void addPort(Port port) {
        if (ports != null) this.ports.add(port);
    }

    public void deleteAllPorts() {
        this.ports.clear();
    }
}