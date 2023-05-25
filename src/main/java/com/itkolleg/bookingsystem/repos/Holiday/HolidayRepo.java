package com.itkolleg.bookingsystem.repos.Holiday;

import com.itkolleg.bookingsystem.domains.Holiday;

import java.time.LocalDate;

public interface HolidayRepo {

    Holiday addHoliday(Holiday holiday);
    void deleteHoliday(Long id);
    boolean isBookingAllowedOnHoliday(LocalDate date);
    Holiday findByDate(LocalDate date);

}
