package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;

/**
 * This class represents a time slot in the system.
 * It provides different static time slot constants which represent typical booking time periods.
 * TimeSlot objects are saved in the database.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TimeSlot {

    private static final Logger logger = LoggerFactory.getLogger(TimeSlot.class);

    /**
     * Morning time slot (8:00 to 12:30)
     */
    @Transient
    public static final TimeSlot AM = new TimeSlot(LocalTime.of(8, 0), LocalTime.of(12, 30), "AM");

    /**
     * Afternoon time slot (12:30 to 17:00)
     */
    @Transient
    public static final TimeSlot PM = new TimeSlot(LocalTime.of(12, 30), LocalTime.of(17, 0), "PM");

    /**
     * Full day time slot (8:00 to 17:00)
     */
    @Transient
    public static final TimeSlot ALL_DAY = new TimeSlot(LocalTime.of(8, 0), LocalTime.of(17, 0), "ALL_DAY");

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalTime startTime;
    private LocalTime endTime;
    private String name;

    /**
     * Constructor to create a TimeSlot.
     *
     * @param startTime The start time of the slot.
     * @param endTime   The end time of the slot.
     * @param name      The name of the slot.
     */
    public TimeSlot(LocalTime startTime, LocalTime endTime, String name) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
    }

    /**
     * Get the start time of the time slot as a string.
     *
     * @return The start time as a string.
     */
    public String getStartTimeAsString() {
        return this.startTime.toString();
    }

    /**
     * Get the end time of the time slot as a string.
     *
     * @return The end time as a string.
     */
    public String getEndTimeAsString() {
        return this.endTime.toString();
    }
}
