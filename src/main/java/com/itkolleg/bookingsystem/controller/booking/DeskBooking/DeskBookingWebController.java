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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("web/deskBookings")
public class DeskBookingWebController {
    private final DeskBookingService deskBookingService;

    private final DeskService deskService;

    private final EmployeeService employeeService;
    private final TimeSlotService timeSlotService;
    private static final Logger logger = LoggerFactory.getLogger(DeskBookingWebController.class);


    public DeskBookingWebController(DeskBookingService deskBookingService, DeskService deskService, EmployeeService employeeService, TimeSlotService timeSlotService) {
        this.deskBookingService = deskBookingService;
        this.deskService = deskService;
        this.employeeService = employeeService;
        this.timeSlotService = timeSlotService;
    }

    @GetMapping
    public String getAllDeskBookings(Model model) {
        model.addAttribute("viewAllDeskBookings", deskBookingService.getAllBookings());
        return "DeskBookings/allDeskBookings";
    }

    @GetMapping("/add")
    public String addDeskBookingForm(Model model) throws ExecutionException, InterruptedException {
        DeskBooking deskBooking = new DeskBooking();
        List<Desk> deskList = deskService.getAllDesks();
        List<Employee> employeeList = employeeService.getAllEmployees();
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
        //Add employees, desks, unique booking start and end times to the model
        model.addAttribute("deskBooking", deskBooking);
        model.addAttribute("employees", employeeList);
        model.addAttribute("desks", deskList);
        model.addAttribute("startTimes", uniqueStartTimes);
        model.addAttribute("endTimes", uniqueEndTimes);
        return "DeskBookings/addDeskBooking";
    }

    @PostMapping("/add")
    public String addDeskBooking(@ModelAttribute("deskBooking") @Valid DeskBooking booking, BindingResult bindingResult, @RequestParam("employee.id") Long employeeId, @RequestParam("desk.id") Long deskId, Model model) throws ExecutionException, InterruptedException {

    try {
            if (bindingResult.hasErrors()) {
                System.out.println("Errors: " + bindingResult.getAllErrors());
                return "DeskBookings/addDeskBooking";
            } else {
                Desk desk = deskService.getDeskById(deskId);
                Employee employee = employeeService.getEmployeeById(employeeId);

                booking.setDesk(desk);
                booking.setEmployee(employee);
                booking.setCreatedOn(LocalDateTime.now());

                this.deskBookingService.addDeskBooking(booking);
                return "redirect:/web/deskBookings";
            }
        } catch (DeskNotAvailableException e) {
            logger.error("Desk is not available for booking: " + e.getMessage(), e);
            model.addAttribute("errorMessage", "Desk is not available for booking.");
            addAttributesToModel(model);
            return "DeskBookings/addDeskBooking";
        } catch (ExecutionException | InterruptedException | EmployeeNotFoundException | ResourceNotFoundException e) {
            logger.error("Error occurred while booking the desk: " + e.getMessage(), e);
            model.addAttribute("errorMessage", "Error occurred while booking the desk.");
            addAttributesToModel(model);
            return "DeskBookings/addDeskBooking";
        } catch (IllegalArgumentException e) {
            logger.error("Cannot create booking for a past date: " + e.getMessage(), e);
            model.addAttribute("errorMessage", "Cannot create booking for a past date.");
            addAttributesToModel(model);
            return "DeskBookings/addDeskBooking";
        }
    }


    private void addAttributesToModel(Model model) throws ExecutionException, InterruptedException {
        List<Desk> deskList = deskService.getAllDesks();
        List<Employee> employeeList = employeeService.getAllEmployees();
        List<TimeSlot> times = timeSlotService.getAllTimeSlots();
        DeskBooking deskBooking = new DeskBooking(); // Added this line

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
        //Add employees, desks, unique booking start and end times to the model
        model.addAttribute("deskBooking", deskBooking); // And this line
        model.addAttribute("employees", employeeList);
        model.addAttribute("desks", deskList);
        model.addAttribute("startTimes", uniqueStartTimes);
        model.addAttribute("endTimes", uniqueEndTimes);
    }


    @GetMapping("/view/{id}")
    public String viewDeskBooking(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        DeskBooking deskBooking = this.deskBookingService.getBookingById(id);
        model.addAttribute("myDeskBooking", deskBooking);
        return "DeskBookings/viewDeskBooking";
    }

    @GetMapping("/update/{id}")
    public String updateDeskBookingForm(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        DeskBooking booking = this.deskBookingService.getBookingById(id);

        if (booking == null) {
            // Log an error message indicating that the booking was not found
            logger.error("Booking with ID {} not found.", id);
            return "redirect:/error";
        }

        // Get the employee, Desks and TimesSlots after checking that the booking is not null
        Employee employee = booking.getEmployee();
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
        model.addAttribute("employee", employee);
        model.addAttribute("desks", desks);
        model.addAttribute("uniqueStartTimes", uniqueStartTimes);
        model.addAttribute("uniqueEndTimes", uniqueEndTimes);

        return "DeskBookings/updateDeskBooking";
    }



    @PostMapping("/update")
    public String updateDeskBooking(@Valid DeskBooking booking, BindingResult bindingResult, @RequestParam("id") Long id, @RequestParam("desk.id") Long deskId, @RequestParam("employee.id") Long employeeId) throws ResourceNotFoundException, DeskNotAvailableException, ExecutionException, InterruptedException, EmployeeNotFoundException {
        if (bindingResult.hasErrors()) {
            System.out.println("Errors: " + bindingResult.getAllErrors());
            return "DeskBookings/updateDeskBooking";
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
            return "redirect:/web/deskBookings";
        }
    }

    @GetMapping("/cancel/{id}")
    public String cancelDeskBooking(@PathVariable Long id, Model model) throws ResourceDeletionFailureException, ResourceNotFoundException {
        try {
            DeskBooking booking = this.deskBookingService.getBookingById(id);
            model.addAttribute("booking", booking);
            this.deskBookingService.deleteBooking(id);
            return "redirect:/web/deskBookings";
        } catch(ResourceDeletionFailureException e) {
            // Log the error
            logger.error("Failed to cancel booking", e);

            // Handle the exception here. This could be logging the error, showing an error message, etc.
            model.addAttribute("errorMessage", "Failed to cancel booking due to: " + e.getMessage());
            return "error"; // return to an error page
        }
    }


    @GetMapping("/mybookings/{id}")
    public String getMyDeskBookings(Model model, @PathVariable Long id) {
        List<DeskBooking> myBookings = this.deskBookingService.getBookingsByEmployeeId(id);
        model.addAttribute("myBookings", myBookings);
        return "DeskBookings/myDeskBookings";
    }

    @GetMapping("/bookinghistory/{id}")
    public String getMyDeskBookingHistory(Model model, @PathVariable Long id) {
        List<DeskBooking> myBookingHistory = this.deskBookingService.getMyBookingHistory(id);
        model.addAttribute("myBookingHistory", myBookingHistory);
        return "DeskBookings/myDeskBookingsHistory";
    }

    @GetMapping("/admin")
    public String getAdminDashboard(Model model) {
        List<DeskBooking> bookings = this.deskBookingService.getAllBookings();
        List<Desk> desks = this.deskService.getAllDesks();
        model.addAttribute("bookings", bookings);
        model.addAttribute("desks", desks);
        // additional items to display on the admin dashboard e.g. All Equipment Bookings & All Room Bookings
        return "DeskBookings/adminDashboard";
    }


    @GetMapping("/error")
    public String getError() {
        return "errorPage";
    }
}
