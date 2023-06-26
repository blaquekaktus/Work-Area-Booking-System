package com.itkolleg.bookingsystem.repos.TimeSlot;

import com.itkolleg.bookingsystem.domains.TimeSlot;
import com.itkolleg.bookingsystem.exceptions.DatabaseOperationException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface for handling operations related to time slots.
 * Includes methods to add, retrieve, update, and delete time slots.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
public interface TimeSlotRepo {

    /**
     * Adds a new time slot.
     *
     * @param timeSlot The time slot to add.
     * @return The added time slot.
     * @throws DatabaseOperationException If the operation fails.
     */
    TimeSlot addTimeSlot(TimeSlot timeSlot) throws DatabaseOperationException;

    /**
     * Retrieves all time slots.
     *
     * @return A list of all time slots.
     * @throws DatabaseOperationException If the operation fails.
     */
    List<TimeSlot> getAllTimeSlots() throws DatabaseOperationException;

    /**
     * Retrieves a time slot by its name.
     *
     * @param name The name of the time slot.
     * @return An Optional of the time slot if it exists, empty Optional otherwise.
     * @throws ResourceNotFoundException If the time slot does not exist.
     */
    Optional<TimeSlot> getTimeSlotByName(String name) throws ResourceNotFoundException;

    /**
     * Retrieves a time slot by its start time.
     *
     * @param startTime The start time of the time slot.
     * @return An Optional of the time slot if it exists, empty Optional otherwise.
     * @throws ResourceNotFoundException If the time slot does not exist.
     */
    Optional<TimeSlot> getTimeSlotByStartTime(LocalTime startTime) throws ResourceNotFoundException;

    /**
     * Retrieves a time slot by its end time.
     *
     * @param endTime The end time of the time slot.
     * @return An Optional of the time slot if it exists, empty Optional otherwise.
     * @throws ResourceNotFoundException If the time slot does not exist.
     */
    Optional<TimeSlot> getTimeSlotByEndTime(LocalTime endTime) throws ResourceNotFoundException;

    /**
     * Updates a time slot.
     *
     * @param timeSlot The time slot with updated information.
     * @return An Optional of the updated time slot if it exists, empty Optional otherwise.
     * @throws DatabaseOperationException If the operation fails.
     */
    Optional<TimeSlot> updateTimeSlot(TimeSlot timeSlot) throws DatabaseOperationException;

    /**
     * Deletes a time slot by its id.
     *
     * @param Id The id of the time slot.
     * @throws DatabaseOperationException If the operation fails.
     */
    void deleteTimeSlotById(Long Id) throws DatabaseOperationException;

    /**
     * Deletes a time slot by its name.
     *
     * @param name The name of the time slot.
     * @throws DatabaseOperationException If the operation fails.
     */
    void deleteTimeSlotByName(String name) throws DatabaseOperationException;

    /**
     * Deletes a specific time slot.
     *
     * @param toDelete The time slot to delete.
     * @throws DatabaseOperationException If the operation fails.
     */
    void delete(TimeSlot toDelete) throws DatabaseOperationException;

}
