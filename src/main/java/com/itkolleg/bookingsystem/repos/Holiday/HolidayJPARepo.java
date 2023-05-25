package com.itkolleg.bookingsystem.repos.Holiday;


import com.itkolleg.bookingsystem.domains.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HolidayJPARepo extends JpaRepository<Holiday, Long> {
    Holiday findByDate(LocalDate date);
}
