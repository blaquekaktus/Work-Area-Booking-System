package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    private Employee employee;

    @ManyToOne(fetch=FetchType.EAGER)
    private Bookable bookable;

    private Date bookingStart;
    private Date bookingEnd;
    private Timestamp bookingTime;
    private String info;

    public Booking(Employee employee, Date bookingStart, Date bookingEnd, Timestamp bookingTime, String info, Bookable bookable) {
        this.employee = employee;
        this.bookingStart = bookingStart;
        this.bookingEnd = bookingEnd;
        this.bookingTime = bookingTime;
        this.info = info;
        this.bookable = bookable;
    }


}