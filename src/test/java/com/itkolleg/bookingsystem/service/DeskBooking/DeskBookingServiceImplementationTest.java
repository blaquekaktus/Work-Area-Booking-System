package com.itkolleg.bookingsystem.service.DeskBooking;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.itkolleg.bookingsystem.domains.Booking.DeskBooking;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.repos.DeskBooking.DeskBookingRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class DeskBookingServiceImplementationTest {

    @Mock
    DeskBookingRepo deskBookingRepo;

    @InjectMocks
    DeskBookingServiceImplementation deskBookingService;

    @Test
    public void testGetBookingById() throws ResourceNotFoundException {
        Long testId = 5L;
        DeskBooking testBooking = new DeskBooking();
        testBooking.setId(testId);

        when(deskBookingRepo.getBookingByBookingId(testId)).thenReturn(Optional.of(testBooking));

        DeskBooking foundBooking = deskBookingService.getBookingById(testId);

        assertNotNull(foundBooking, "Expected non-null booking");
        assertEquals(testId, foundBooking.getId(), "Booking id did not match expected id");
    }
}