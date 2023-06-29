package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class Room {

    private static final Logger logger = LoggerFactory.getLogger(Room.class);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String floor;

    private String info;

    /*@OneToMany(mappedBy = "room")
    private List<Desk> desks;*/

    public Room(Long id, String floor) {
        this.id = id;
        this.floor = floor;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "roomId = " + id + ", " +

                "floor = " + floor + ", ";
    }


}