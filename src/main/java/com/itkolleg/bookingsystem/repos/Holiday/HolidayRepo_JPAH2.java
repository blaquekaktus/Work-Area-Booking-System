package com.itkolleg.bookingsystem.repos.Holiday;

import com.itkolleg.bookingsystem.domains.Holiday;
import com.itkolleg.bookingsystem.repos.DeskBooking.DeskBookingRepo_JPAH2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@ComponentScan({"com.itkolleg.repos"})
public class HolidayRepo_JPAH2 implements HolidayRepo{
    private static final Logger logger = LoggerFactory.getLogger(DeskBookingRepo_JPAH2.class);
    private final HolidayJPARepo holidayJPARepo;

    public HolidayRepo_JPAH2(HolidayJPARepo holidayJPARepo) {
        this.holidayJPARepo = holidayJPARepo;
    }

    @Override
    public void addHoliday(Holiday holiday) {
        this.holidayJPARepo.save(holiday);

    }

    @Override
    public List<Holiday> getAllHolidays() {
        return this.holidayJPARepo.findAll();
    }

    @Override
    public void deleteHoliday(Long id) {
        this.holidayJPARepo.deleteById(id);
    }

    @Override
    public boolean isBookingAllowedOnHoliday(LocalDate date) {
        Holiday holiday = this.holidayJPARepo.findByDate(date);
        System.out.println("Holiday: " + holiday); // Debugging statement
        // If holiday object is null (no entry in Holiday table), consider it a normal day and allow bookings
        return holiday == null || holiday.isBookingAllowed();
    }

    @Override
    public Holiday findByDate(LocalDate date) {
        return this.holidayJPARepo.findByDate(date);
    }
}
