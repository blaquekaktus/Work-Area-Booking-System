package com.itkolleg.bookingsystem.service.Holiday;

import com.itkolleg.bookingsystem.domains.Holiday;
import com.itkolleg.bookingsystem.repos.Holiday.HolidayRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service

public class HolidayServiceImplementation implements HolidayService{

    @Autowired
    private HolidayRepo holidayRepo;

    public HolidayServiceImplementation(HolidayRepo holidayRepo) {
        this.holidayRepo = holidayRepo;
    }

    @Override
    public void addHoliday(Holiday holiday) {
         this.holidayRepo.addHoliday(holiday);
    }

    public void deleteHoliday(Long id) {
        holidayRepo.deleteHoliday(id);
    }

    public List<Holiday> getAllHolidays(){
        return this.holidayRepo.getAllHolidays();
    }

    public boolean isBookingAllowedOnHoliday(LocalDate date) {
        Holiday holiday = holidayRepo.findByDate(date);
        return holiday != null && holiday.isBookingAllowed();
    }
}




