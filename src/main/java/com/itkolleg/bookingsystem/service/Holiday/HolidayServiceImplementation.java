package com.itkolleg.bookingsystem.service.Holiday;

import com.itkolleg.bookingsystem.domains.PublicHoliday;
import com.itkolleg.bookingsystem.repos.Holiday.HolidayRepo;
import com.itkolleg.bookingsystem.service.Desk.DeskServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 Service implementation for managing holidays.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-26
 */
@Service
public class HolidayServiceImplementation implements HolidayService {

    static final Logger logger = LoggerFactory.getLogger(HolidayServiceImplementation.class);
    private final HolidayRepo holidayRepo;

    @Autowired
    public HolidayServiceImplementation(HolidayRepo holidayRepo) {
        this.holidayRepo = holidayRepo;
    }

    /**
     * Adds a new public holiday.
     *
     * @param publicHoliday The public holiday to add.
     */
    @Override
    public void addHoliday(PublicHoliday publicHoliday) {
        this.holidayRepo.addHoliday(publicHoliday);
    }

    /**
     * Deletes a public holiday by its ID.
     *
     * @param id The ID of the public holiday to delete.
     */
    public void deleteHoliday(Long id) {
        holidayRepo.deleteHoliday(id);
    }

    /**
     * Retrieves all public holidays.
     *
     * @return The list of all public holidays.
     */
    public List<PublicHoliday> getAllHolidays() {
        return this.holidayRepo.getAllHolidays();
    }

    /**
     * Checks if booking is allowed on a specific holiday date.
     *
     * @param date The date to check.
     * @return true if booking is allowed on the holiday date, false otherwise.
     */
    public boolean isBookingAllowedOnHoliday(LocalDate date) {
        PublicHoliday publicHoliday = holidayRepo.findByDate(date);
        return publicHoliday != null && publicHoliday.isBookingAllowed();
    }
}


