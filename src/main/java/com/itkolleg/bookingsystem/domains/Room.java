package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long roomId;

    @ElementCollection
    private List<Point> vertices = new ArrayList<>();

    private String floor;

    private String info;

    @OneToMany(mappedBy = "room")
    private List<Desk> desks;

    public Room(List<Point> vertices, String floor, String info) {

        this.vertices = vertices;
        this.floor = floor;
        this.info = info;
    }

    /*
        public void deserialize(Map<String, Object> data) {
            floor = (String) data.get("floor");
            info = (String) data.get("info");
            ArrayList<Map<String, Long>> locations = (ArrayList<Map<String, Long>>) data.get("vertices");
            vertices = new ArrayList<>();
            for (Map<String, Long> location : locations) {
                long x = location.get("x");
                long y = location.get("y");
                vertices.add(new Point((int) x, (int) y));
            }
        } */

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "roomId = " + roomId + ", " +
                "vertices = " + vertices + ", " +
                "floor = " + floor + ", " +
                "info = " + info + ")";
    }
}