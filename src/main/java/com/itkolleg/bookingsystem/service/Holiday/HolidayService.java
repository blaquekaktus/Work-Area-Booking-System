package com.itkolleg.bookingsystem.service.Holiday;

import com.itkolleg.bookingsystem.domains.Holiday;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public interface HolidayService {


    Logger logger = LoggerFactory.getLogger(com.itkolleg.bookingsystem.service.Holiday.HolidayService.class);


    void addHoliday(Holiday holiday);
    void deleteHoliday(Long id);
    boolean isBookingAllowedOnHoliday(LocalDate date);

}