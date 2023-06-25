package com.itkolleg.bookingsystem.service.TimeSlot;

import com.itkolleg.bookingsystem.domains.TimeSlot;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.repos.TimeSlot.TimeSlotRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the TimeSlotService interface.
 * This class contains the business logic associated with TimeSlots.
 */
@Service
public class TimeSlotServiceImplementation implements TimeSlotService {

    private static final Logger logger = LoggerFactory.getLogger(TimeSlotServiceImplementation.class);
    private final TimeSlotRepo timeSlotRepo;

    /**
     * Constructs a TimeSlotServiceImplementation instance.
     *
     * @param timeSlotRepo      Repository for accessing TimeSlot data.
    */
    public TimeSlotServiceImplementation(TimeSlotRepo timeSlotRepo) {
        this.timeSlotRepo = timeSlotRepo;
    }

    /**
     * Adds a new TimeSlot.
     *
     * @param timeSlot  The TimeSlot to add.
     * @return The added TimeSlot.
     */
    @Override
    public TimeSlot addTimeSlot(TimeSlot timeSlot) {
        return this.timeSlotRepo.addTimeSlot(timeSlot);
    }

    /**
     * Retrieves all TimeSlots.
     *
     * @return A list of all TimeSlots.
     */
    @Override
    public List<TimeSlot> getAllTimeSlots() {
        return this.timeSlotRepo.getAllTimeSlots();
    }

    /**
     * Retrieves a TimeSlot by its name.
     *
     * @param name  The name of the TimeSlot.
     * @return An Optional containing the found TimeSlot, or empty if no TimeSlot was found.
     */
    @Override
    public Optional<TimeSlot> getTimeSlotByName(String name) {
        try {
            return this.timeSlotRepo.getTimeSlotByName(name);
        } catch (ResourceNotFoundException e) {
            logger.error("TimeSlot with name '{}' not found", name, e);
            return Optional.empty();
        }
    }
    /**
     * Retrieves a TimeSlot by its start time.
     *
     * @param startTime  The start time of the TimeSlot.
     * @return An Optional containing the found TimeSlot, or empty if no TimeSlot was found.
     */
    @Override
    public Optional<TimeSlot> getTimeSlotByStartTime(LocalTime startTime){
        try{
        return this.timeSlotRepo.getTimeSlotByStartTime(startTime);
        } catch (ResourceNotFoundException e) {
            logger.error("TimeSlot with start time '{}' not found",startTime, e);
            return Optional.empty();
        }
    }

    /**
     * Retrieves a TimeSlot by its end time.
     *
     * @param endTime  The end time of the TimeSlot.
     * @return An Optional containing the found TimeSlot, or empty if no TimeSlot was found.
     */
    @Override
    public Optional<TimeSlot> getTimeSlotByEndTime(LocalTime endTime){
        try{
        return this.timeSlotRepo.getTimeSlotByEndTime(endTime);
        } catch (ResourceNotFoundException e) {
            logger.error("TimeSlot with endTime '{}' not found", endTime, e);
            return Optional.empty();
        }
    }

    /**
     * Updates a TimeSlot.
     *
     * @param timeSlot  The TimeSlot to update.
     * @return An Optional containing the updated TimeSlot, or empty if no TimeSlot was found.
     */
    @Override
    public Optional<TimeSlot> updateTimeSlot(TimeSlot timeSlot){
        return this.timeSlotRepo.updateTimeSlot(timeSlot);
    }

    /**
     * Deletes a TimeSlot by its id.
     *
     * @param id  The id of the TimeSlot to delete.
     */
    @Override
    public void deleteTimeSlotById(Long id){
        this.timeSlotRepo.deleteTimeSlotById(id);
    }

    /**
     * Deletes a TimeSlot by its name.
     * If the TimeSlot with the given name does not exist, an error is logged and no action is taken.
     *
     * @param name  The name of the TimeSlot to delete.
     */
    @Override
    public void deleteTimeSlotByName(String name){
        try {
            TimeSlot toDelete = this.timeSlotRepo.getTimeSlotByName(name)
                    .orElseThrow(() -> new IllegalArgumentException("No timeslot found with the given name: " + name));
            this.timeSlotRepo.delete(toDelete);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            logger.error("TimeSlot with name '{}' not found", name, e);
        }
    }
}