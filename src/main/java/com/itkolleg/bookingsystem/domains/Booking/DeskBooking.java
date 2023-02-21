package com.itkolleg.bookingsystem.domains.Booking;


import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeskBooking extends Booking {

    @ManyToOne (fetch = FetchType.EAGER)
    private Desk desk;

    public DeskBooking(Employee employee, Desk desk, LocalDateTime bookingStart, LocalDateTime bookingEnd, LocalDateTime bookingTime){
        super(employee, bookingStart, bookingEnd, bookingTime);
        this.desk=desk;

    }

}
