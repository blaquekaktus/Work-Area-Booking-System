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


    /*@OneToMany(mappedBy = "room")
    private List<Desk> desks;*/

    public Room(Long id,String floor) {
        this.id=id;
        this.floor = floor;
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
                "roomId = " + id + ", " +

                "floor = " + floor + ", ";
    }


}