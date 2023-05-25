package com.itkolleg.bookingsystem.controller.booking;

import com.itkolleg.bookingsystem.service.Desk.DeskService;
import com.itkolleg.bookingsystem.service.DeskBooking.DeskBookingService;
import com.itkolleg.bookingsystem.service.Employee.EmployeeService;
import com.itkolleg.bookingsystem.service.TimeSlot.TimeSlotService;
import com.itkolleg.bookingsystem.domains.Booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.TimeSlot;
import com.itkolleg.bookingsystem.exceptions.BookingExceptions.BookingNotFoundException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskNotFoundException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.itkolleg.bookingsystem.service.DeskBooking.DeskBookingService.logger;

@Controller
@RequestMapping("web/deskBookings")
public class DeskBookingWebController {
    private final DeskBookingService deskBookingService;

    private final DeskService deskService;

    private final EmployeeService employeeService;
    private final TimeSlotService timeSlotService;


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
        List<TimeSlot> timeSlotList = timeSlotService.getAllTimeSlots();
        model.addAttribute("deskBooking", deskBooking);
        model.addAttribute("deskList", deskList);
        model.addAttribute("employeeList", employeeList);
        model.addAttribute("timeSlotList", timeSlotList);
        return "DeskBookings/addDeskBooking";
    }

    @PostMapping("/add")
    public String addDeskBooking(@Valid DeskBooking booking, BindingResult bindingResult,
                                 @RequestParam("employee.id") Long employeeId,
                                 @RequestParam("desk.id") Long deskId) throws ExecutionException, InterruptedException, EmployeeNotFoundException, DeskNotFoundException, DeskNotAvailableException {
        if (bindingResult.hasErrors()) {
            return "DeskBookings/addDeskBooking";
        } else {
            Employee employee = employeeService.getEmployeeById(employeeId);
            Desk desk = deskService.getDeskById(deskId);

            booking.setEmployee(employee);
            booking.setDesk(desk);

            this.deskBookingService.addDeskBooking(booking);
            return "redirect:/web/deskBookings";
        }
    }

    @GetMapping("/view/{id}")
    public String viewDeskBooking(@PathVariable Long id, Model model) throws BookingNotFoundException {
        DeskBooking deskBooking = this.deskBookingService.getBookingById(id);
        model.addAttribute("myDeskBooking", deskBooking);
        return "DeskBookings/viewDeskBooking";
    }

    @GetMapping("/update/{id}")
    public String updateDeskBookingForm(@PathVariable Long id, Model model) throws BookingNotFoundException {
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
    public String updateDeskBooking(@Valid DeskBooking booking, BindingResult bindingResult, @RequestParam("id") Long id, @RequestParam("desk.id") Long deskId, @RequestParam("employee.id") Long employeeId) throws BookingNotFoundException, DeskNotAvailableException, DeskNotFoundException, ExecutionException, InterruptedException, EmployeeNotFoundException {
        if (bindingResult.hasErrors()) {
            System.out.println("Errors: " + bindingResult.getAllErrors());
            return "DeskBookings/updateDeskBooking";
        } else {
            Desk desk = deskService.getDeskById(deskId);
                if(desk == null) {
                    throw new DeskNotFoundException("Desk not found for id: " + deskId);
                }
            Employee employee = employeeService.getEmployeeById(employeeId);
                if(employee == null) {
                    throw new EmployeeNotFoundException("Employee not found for id: " + employeeId);
                }

            booking.setDesk(desk);
            booking.setEmployee(employee);
            booking.setId(id);
            booking.setTimeStamp(LocalDateTime.now());

            this.deskBookingService.updateBooking(booking);
            return "redirect:/web/deskBookings";
        }
    }

    @GetMapping("/delete/{id}")
    public String cancelDeskbooking(@PathVariable Long id, Model model) throws BookingNotFoundException {
        DeskBooking booking = this.deskBookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        this.deskBookingService.deleteBooking(id);
        return "redirect:/web/deskBookings";
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
