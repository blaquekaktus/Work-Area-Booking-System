package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalTime;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String name;
    public static final TimeSlot AM = new TimeSlot( LocalTime.of(8, 0), LocalTime.of(12, 30), "AM");
    public static final TimeSlot PM = new TimeSlot( LocalTime.of(12, 30), LocalTime.of(17, 0),"PM");
    public static final TimeSlot ALL_DAY = new TimeSlot(  LocalTime.of(8, 0), LocalTime.of(17, 0), "ALL_DAY");


    public TimeSlot(LocalTime startTime, LocalTime endTime, String name) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getStartTimeAsString()
    {
        return this.startTime.toString();
    }

    public String getEndTimeAsString()
    {
        return this.endTime.toString();
    }
}