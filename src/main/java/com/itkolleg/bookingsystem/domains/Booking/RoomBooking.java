package com.itkolleg.bookingsystem.domains.Booking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.domains.TimeSlot;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Setter
public class RoomBooking extends Booking {
    @ManyToOne
    private Room room;

    public RoomBooking(Employee employee, Room room, LocalDate date, LocalTime start, LocalTime endTime){
        super(employee,date,start,endTime);
        this.room=room;
    }

}