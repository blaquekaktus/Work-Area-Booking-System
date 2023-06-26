package com.itkolleg.bookingsystem.domains.Booking;

import com.itkolleg.bookingsystem.domains.Desk;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private static final Logger logger = LoggerFactory.getLogger(BookingResponse.class);

    private boolean success;
    private String errorMessage;

}





