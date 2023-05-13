package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Desk {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    private String deskNr;

    private int nrOfMonitors;

    @ElementCollection
    private List<String> ports;

    public Desk(String deskNr, int nrOfMonitors, List<String> ports){
        this.deskNr=deskNr;
        this.nrOfMonitors=nrOfMonitors;
        this.ports=ports;
    }
}
