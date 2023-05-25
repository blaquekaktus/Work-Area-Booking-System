package com.itkolleg.bookingsystem.repos.Desk;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Port;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface DeskRepo {
    Desk addDesk(Desk desk);

    List<Desk> getAllDesks();

    int getTotalDesks();

    Page<Desk> getAllDesksByPage(Pageable pageable);

    Desk getDeskById(Long id) throws DeskNotFoundException;

    Desk updateDeskById(Long id, Desk desk) throws DeskNotFoundException;

    Desk updateDesk(Desk desk) throws DeskNotFoundException;

    List<Desk> deleteDeskById(Long id) throws DeskDeletionFailureException;

    Desk createPort(Long deskId, Port newPort);

    Desk updatePort(Long deskId, String portName, Port updatedPort);

    Desk deletePort(Long deskId, String portName);

    List<Port> getPorts(Long deskId);

}