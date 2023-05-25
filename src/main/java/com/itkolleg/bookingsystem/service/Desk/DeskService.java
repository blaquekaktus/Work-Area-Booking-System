package com.itkolleg.bookingsystem.service.Desk;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Port;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The DeskService interface provides operations for managing desks and ports in a booking system.
 */
public interface DeskService {

    /**
     * Logger instance for logging.
     */
    Logger logger = LoggerFactory.getLogger(DeskService.class);

    /**
     * Adds a new desk to the booking system.
     *
     * @param desk The desk to be added.
     * @return The added desk.
     */
    Desk addDesk(Desk desk);

    /**
     * Retrieves all desks in the booking system.
     *
     * @return A list of all desks.
     */
    List<Desk> getAllDesks();

    /**
     * Retrieves the total number of desks in the booking system.
     *
     * @return The total number of desks.
     */
    int getTotalDesks();

    /**
     * Retrieves a page of desks using pagination.
     *
     * @param pageable The pageable object containing pagination information.
     * @return A page of desks.
     */
    Page<Desk> getAllDesksByPage(Pageable pageable);

    /**
     * Retrieves a desk by its ID.
     *
     * @param id The ID of the desk.
     * @return The desk with the specified ID.
     * @throws DeskNotFoundException if the desk is not found.
     */
    Desk getDeskById(Long id) throws DeskNotFoundException;

    /**
     * Updates a desk with the specified ID.
     *
     * @param id   The ID of the desk to be updated.
     * @param desk The updated desk object.
     * @return The updated desk.
     * @throws DeskNotFoundException if the desk is not found.
     */
    Desk updateDeskById(Long id, Desk desk) throws DeskNotFoundException;

    /**
     * Updates a desk.
     *
     * @param desk The updated desk object.
     * @return The updated desk.
     * @throws DeskNotFoundException if the desk is not found.
     */
    Desk updateDesk(Desk desk) throws DeskNotFoundException;

    /**
     * Deletes a desk with the specified ID.
     *
     * @param id The ID of the desk to be deleted.
     * @return A list of desks after the deletion.
     * @throws DeskDeletionFailureException if the desk deletion fails.
     */
    List<Desk> deleteDeskById(Long id) throws DeskDeletionFailureException;

    /**
     * Creates a new port for a desk.
     *
     * @param deskId  The ID of the desk.
     * @param newPort The new port to be created.
     * @return The desk with the newly created port.
     */
    Desk createPort(Long deskId, Port newPort);

    /**
     * Updates an existing port for a desk.
     *
     * @param deskId      The ID of the desk.
     * @param portName    The name of the port to be updated.
     * @param updatedPort The updated port object.
     * @return The desk with the updated port.
     */
    Desk updatePort(Long deskId, String portName, Port updatedPort);

    /**
     * Deletes a port from a desk.
     */

    Desk deletePort(Long deskId, String portName);

    /**
     * Retrieves the list of ports for a desk.
     *
     * @param deskId The ID of the desk.
     * @return The list of ports for the specified desk.
     */
    List<Port> getPorts(Long deskId);

}