package com.itkolleg.bookingsystem.repos.Holiday;

import com.itkolleg.bookingsystem.domains.Holiday;

import java.time.LocalDate;
import java.util.List;

public interface HolidayRepo {

    void addHoliday(Holiday holiday);
    List<Holiday> getAllHolidays();
    void deleteHoliday(Long id);
    boolean isBookingAllowedOnHoliday(LocalDate date);
    Holiday findByDate(LocalDate date);

}
