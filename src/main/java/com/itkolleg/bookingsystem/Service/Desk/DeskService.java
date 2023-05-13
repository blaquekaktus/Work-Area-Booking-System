package com.itkolleg.bookingsystem.Service.Desk;

import java.util.List;

public interface DeskService {

    Logger logger = LoggerFactory.getLogger(DeskService.class);
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
    List<Port>getPorts(Long deskId);
}