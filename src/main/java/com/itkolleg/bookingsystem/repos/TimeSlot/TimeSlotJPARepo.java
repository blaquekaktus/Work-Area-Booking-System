package com.itkolleg.bookingsystem.repos.TimeSlot;

import com.itkolleg.bookingsystem.domains.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

/**
 * TimeSlotJPARepo provides an API for CRUD operations on TimeSlots.
 * It extends JpaRepository, which provides JPA related methods out of the box for CRUD operations.
 */
@Repository
public interface TimeSlotJPARepo extends JpaRepository<TimeSlot, Long> {

    /**
     * Retrieves a TimeSlot by its start time.
     *
     * @param startTime The start time of the TimeSlot.
     * @return An Optional containing the found TimeSlot, or empty if no TimeSlot was found with the given start time.
     */
    Optional<TimeSlot> findByStartTime(LocalTime startTime);

    /**
     * Retrieves a TimeSlot by its end time.
     *
     * @param endTime The end time of the TimeSlot.
     * @return An Optional containing the found TimeSlot, or empty if no TimeSlot was found with the given end time.
     */
    Optional<TimeSlot> findByEndTime(LocalTime endTime);
    /**
     * Retrieves a TimeSlot by its name
     * .
     *
     * @param name The name of the TimeSlot.
     * @return An Optional containing the found TimeSlot, or empty if no TimeSlot was found with the given name.
     */
    Optional<TimeSlot> findByName(String name);

    void deleteTimeSlotByName(String name);
}