package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class TimeSlot {

    @Transient
    public static final TimeSlot AM = new TimeSlot(LocalTime.of(8, 0), LocalTime.of(12, 30), "AM");
    @Transient
    public static final TimeSlot PM = new TimeSlot(LocalTime.of(12, 30), LocalTime.of(17, 0), "PM");
    @Transient
    public static final TimeSlot ALL_DAY = new TimeSlot(LocalTime.of(8, 0), LocalTime.of(17, 0), "ALL_DAY");
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String name;


    public TimeSlot(LocalTime startTime, LocalTime endTime, String name) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
    }


    public String getStartTimeAsString() {
        return this.startTime.toString();
    }

    public String getEndTimeAsString() {
        return this.endTime.toString();
    }
}