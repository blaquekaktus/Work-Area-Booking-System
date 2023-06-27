package com.itkolleg.bookingsystem.controller.booking.DeskBooking;

import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.service.Desk.DeskService;
import com.itkolleg.bookingsystem.service.DeskBooking.DeskBookingService;
import com.itkolleg.bookingsystem.service.Employee.EmployeeService;
import com.itkolleg.bookingsystem.service.TimeSlot.TimeSlotService;
import com.itkolleg.bookingsystem.domains.Booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.TimeSlot;
import com.itkolleg.bookingsystem.exceptions.DeskNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web/deskbookings")
public class DeskBookingWebController {
    private final DeskBookingService deskBookingService;

    private final DeskService deskService;

    private final EmployeeService employeeService;
    private final TimeSlotService timeSlotService;
    private static final Logger logger = LoggerFactory.getLogger(DeskBookingWebController.class);
    /*private DeskBooking booking;
    private BindingResult bindingResult;
    private Long id;
    private Long deskId;
    private Long employeeId;*/


    public DeskBookingWebController(DeskBookingService deskBookingService, DeskService deskService, EmployeeService employeeService, TimeSlotService timeSlotService) {
        this.deskBookingService = deskBookingService;
        this.deskService = deskService;
        this.employeeService = employeeService;
        this.timeSlotService = timeSlotService;
    }

    @GetMapping("/admin")
    public String getAllDeskBookings(Model model) {
        model.addAttribute("viewAllDeskBookings", deskBookingService.getAllBookings());
        return "DeskBookings/Admin/allDeskBookings";
    }

    @GetMapping("/admin/add")
    public String addDeskBookingForm(Model model) throws ExecutionException, InterruptedException {
        addAttributesToModel(model);
        return "DeskBookings/Admin/addDeskBooking";
    }

    @GetMapping("/add")
    public String addDeskBookingFormForEmployee(Model model) throws ExecutionException, InterruptedException {
        addAttributesToModel(model);
        return "DeskBookings/addDeskBooking";
    }
    @PostMapping("/add")
    public String addDeskBooking(@ModelAttribute("deskBooking") @Valid DeskBooking booking, BindingResult bindingResult, @RequestParam("employee.id") Long employeeId, @RequestParam("desk.id") Long deskId, Model model) throws ExecutionException, InterruptedException {
        try {
            if (bindingResult.hasErrors()) {
                logger.error("Validation errors: {}", bindingResult.getAllErrors());
                return "DeskBookings/Admin/addDeskBooking";
            }

            Desk desk = deskService.getDeskById(deskId);
            Employee employee = employeeService.getEmployeeById(employeeId);

            booking.setDesk(desk);
            booking.setEmployee(employee);

            this.deskBookingService.addDeskBooking(booking);
            return "redirect:/web/deskbookings/admin";
        } catch (Exception e) {
            logger.error("Error occurred while booking the desk: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", e.getMessage());
            addAttributesToModel(model);
            return "DeskBookings/Admin/addDeskBooking";
        }
    }

    private void addAttributesToModel(Model model) throws ExecutionException, InterruptedException {
        DeskBooking deskBooking = new DeskBooking();
        List<Desk> deskList = deskService.getAllDesks();
        List<Employee> employeeList = employeeService.getAllEmployees();
        List<TimeSlot> times = timeSlotService.getAllTimeSlots();

        List<String> uniqueStartTimes = times.stream()
                .map(TimeSlot::getStartTimeAsString)
                .distinct()
                .collect(Collectors.toList());

        List<String> uniqueEndTimes = times.stream()
                .map(TimeSlot::getEndTimeAsString)
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("deskBooking", deskBooking);
        model.addAttribute("employees", employeeList);
        model.addAttribute("desks", deskList);
        model.addAttribute("startTimes", uniqueStartTimes);
        model.addAttribute("endTimes", uniqueEndTimes);
    }


    @GetMapping("/admin/view/{id}")
    public String viewDeskBooking(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        DeskBooking deskBooking = this.deskBookingService.getBookingById(id);
        model.addAttribute("deskBooking", deskBooking);
        return "DeskBookings/Admin/viewDeskBooking";
    }

    @GetMapping("/admin/update/{id}")
    public String updateDeskBookingForm(@PathVariable Long id, Model model) throws ResourceNotFoundException, ExecutionException, InterruptedException {
        DeskBooking booking = this.deskBookingService.getBookingById(id);

        if (booking == null) {
            // Log an error message indicating that the booking was not found
            logger.error("Booking with ID {} not found.", id);
            return "redirect:/error";
        }

        // Get the employee, Desks and TimesSlots after checking that the booking is not null
        List<Employee> employees = this.employeeService.getAllEmployees();
        List<Desk> desks = this.deskService.getAllDesks();
        List<TimeSlot> times = timeSlotService.getAllTimeSlots();

        // Creating list of unique start times
        List<String> uniqueStartTimes = times.stream()
                .map(TimeSlot::getStartTimeAsString)
                .distinct()
                .collect(Collectors.toList());
        // Creating list of unique end times
        List<String> uniqueEndTimes = times.stream()
                .map(TimeSlot::getEndTimeAsString)
                .distinct()
                .collect(Collectors.toList());
        //Add booking, employee, desks, unique booking start and end times to the model
        model.addAttribute("booking", booking);
        model.addAttribute("employees", employees);
        model.addAttribute("desks", desks);
        model.addAttribute("uniqueStartTimes", uniqueStartTimes);
        model.addAttribute("uniqueEndTimes", uniqueEndTimes);
        return "DeskBookings/Admin/updateDeskBooking";
    }

    @PostMapping("/admin/update")
    public String updateDeskBooking(@Valid DeskBooking booking, BindingResult bindingResult, @RequestParam("id") Long id, @RequestParam("desk.id") Long deskId, @RequestParam("employee.id") Long employeeId) throws ResourceNotFoundException, DeskNotAvailableException, ExecutionException, InterruptedException, EmployeeNotFoundException {
        if (bindingResult.hasErrors()) {
            System.out.println("Errors: " + bindingResult.getAllErrors());
            return "DeskBookings/Admin/updateDeskBooking";
        } else {
            Desk desk = deskService.getDeskById(deskId);
            if(desk == null) {
                throw new ResourceNotFoundException("Desk not found for id: " + deskId);
            }
            Employee employee = employeeService.getEmployeeById(employeeId);
            if(employee == null) {
                throw new EmployeeNotFoundException("Employee not found for id: " + employeeId);
            }
            booking.setDesk(desk);
            booking.setEmployee(employee);
            booking.setId(id);
            booking.setCreatedOn(LocalDateTime.now());

            this.deskBookingService.updateBookingById(booking.getId(), booking);
            return "redirect:/web/deskbookings/admin";
        }
    }

    @GetMapping("/admin/cancel/{id}")
    public String cancelDeskBookingForm(@PathVariable Long id, Model model) throws ResourceDeletionFailureException, ResourceNotFoundException {
        DeskBooking booking = this.deskBookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        return "DeskBookings/Admin/cancelDeskBooking";
    }

    @PostMapping("/admin/cancel/{id}")
    public String cancelDeskBooking(@PathVariable Long id, Model model) {
        try {
            DeskBooking booking = this.deskBookingService.getBookingById(id);
            if (booking == null) {
                // Log an error message indicating that the booking was not found
                logger.error("Booking with ID {} not found.", id);
                return "redirect:/error";
            }

            this.deskBookingService.deleteBooking(id);
            return "redirect:/web/deskbookings/admin";
        } catch (ResourceDeletionFailureException e) {
            logger.error("Failed to delete desk booking with ID {}: {}", id, e.getMessage());
            model.addAttribute("errorMessage", "Failed to cancel the desk booking.");
            return "DeskBookings/Admin/cancelDeskBooking";
        } catch (ResourceNotFoundException e) {
            logger.error("Booking with ID {} not found: {}", id, e.getMessage());
            model.addAttribute("errorMessage", "Booking not found.");
            return "redirect:/error";
        }
    }

    @GetMapping("/mydeskbookings")
    public String getBookings(Model model, Authentication authentication) {
        String username = authentication.getName();
        Employee employee = employeeService.findByNick(username);
        Long employeeId = employee.getId();

        // fetch bookings related to this employee
        List<DeskBooking> myBookings = deskBookingService.getBookingsByEmployeeId(employeeId);

        // add the bookings to the model
        model.addAttribute("myDeskBookings", myBookings);

        return "DeskBookings/myDeskBookings";
    }

    /*    @GetMapping("/mybookings/{id}")
    public String getMyDeskBookings(Model model, @PathVariable Long id) {
        List<DeskBooking> myBookings = this.deskBookingService.getBookingsByEmployeeId(id);
        model.addAttribute("myBookings", myBookings);
        return "DeskBookings/myDeskBookings";
    }*/

    @GetMapping("/bookinghistory/{id}")
    public String getMyDeskBookingHistory(Model model, @PathVariable Long id) {
        List<DeskBooking> myBookingHistory = this.deskBookingService.getMyBookingHistory(id);
        model.addAttribute("myBookingHistory", myBookingHistory);
        return "DeskBookings/myDeskBookingsHistory";
    }


    @GetMapping("/error")
    public String getError() {
        return "error";
    }
}
