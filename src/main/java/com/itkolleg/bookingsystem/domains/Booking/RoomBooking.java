package com.itkolleg.bookingsystem.domains.Booking;

import com.itkolleg.bookingsystem.domains.Booking.Booking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Room;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomBooking extends Booking {

    private Room room;

    public RoomBooking(Employee employee, Room room, LocalDateTime bookingStart, LocalDateTime bookingEnd, LocalDateTime bookingTime){
        super(employee, bookingStart, bookingEnd, bookingTime);
        this.room=room;
    }

}