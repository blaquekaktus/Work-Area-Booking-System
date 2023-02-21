package com.itkolleg.bookingsystem.domains.Booking;

import com.itkolleg.bookingsystem.domains.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public abstract class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    //@JoinColumn(name = "employee_id")
    private Employee employee;



    private LocalDateTime bookingStart;
    private LocalDateTime bookingEnd;
    private LocalDateTime bookingTime;

    public Booking(Employee employee, LocalDateTime bookingStart, LocalDateTime bookingEnd, LocalDateTime bookingTime){
        this.employee=employee;
        this.bookingStart=bookingStart;
        this.bookingEnd=bookingEnd;
        this.bookingTime=bookingTime;
    }
}
