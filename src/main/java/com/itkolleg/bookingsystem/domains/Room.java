package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.*;
import lombok.*;
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
    private List<String> vertices = new ArrayList<>();

    private String floor;

    private String info;

    public Room(List<String> vertices, String floor, String info){
        this.vertices=vertices;
        this.floor=floor;
        this.info=info;
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

}