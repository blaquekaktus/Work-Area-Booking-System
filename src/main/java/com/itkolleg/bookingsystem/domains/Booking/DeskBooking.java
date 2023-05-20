package com.itkolleg.bookingsystem.domains.Booking;


import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class DeskBooking extends Booking {

    @ToString.Include
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Desk desk;

    public DeskBooking(Employee employee, Desk desk, LocalDate date, LocalTime startTIme, LocalTime endTime, LocalTime timeStamp) {
        super(employee, date, startTIme, endTime, timeStamp);
        this.desk = desk;
    }

    /*public DeskBooking(Employee employee, Desk desk, LocalDate date, TimeSlot timeSlot, LocalTime timeStamp) {
        super(employee, date, timeSlot, timeStamp);
        this.desk = desk;
    }*/
}