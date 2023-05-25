package com.itkolleg.bookingsystem.service.Holiday;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Holiday;
import com.itkolleg.bookingsystem.repos.Desk.DeskRepo;
import com.itkolleg.bookingsystem.repos.Holiday.HolidayRepo;
import com.itkolleg.bookingsystem.service.Desk.DeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service

public class HolidayServiceImplementation implements HolidayService{

    @Autowired
    private final HolidayRepo holidayRepo;

    public HolidayServiceImplementation(HolidayRepo holidayRepo) {
        this.holidayRepo = holidayRepo;
    }

    @Override
    public void addHoliday(Holiday holiday) {
        holidayRepo.addHoliday(holiday);
    }

    public void deleteHoliday(Long id) {
        holidayRepo.deleteHoliday(id);
    }

    public boolean isBookingAllowedOnHoliday(LocalDate date) {
        Holiday holiday = holidayRepo.findByDate(date);
        return holiday != null && holiday.isBookingAllowed();
    }
}




