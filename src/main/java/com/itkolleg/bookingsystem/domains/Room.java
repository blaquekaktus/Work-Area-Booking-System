package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.*;
import lombok.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Room {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    @ElementCollection
    private ArrayList <Point> vertices;

    private String floor;

    private String info;

    public Room(ArrayList <Point> vertices, String floor, String info){
        this.vertices=vertices;
        this.floor=floor;
        this.info=info;
    }

}