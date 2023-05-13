package com.itkolleg.bookingsystem.controller.booking;

import com.itkolleg.bookingsystem.Service.Desk.DeskService;
import com.itkolleg.bookingsystem.Service.DeskBooking.DeskBookingService;
import com.itkolleg.bookingsystem.Service.Employee.EmployeeService;
import com.itkolleg.bookingsystem.Service.TimeSlot.TimeSlotService;
import com.itkolleg.bookingsystem.domains.Booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.TimeSlot;
import com.itkolleg.bookingsystem.exceptions.BookingExceptions.BookingNotFoundException;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskNotAvailableException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("web/v1/deskBookings")
public class DeskBookingWebController {
    private DeskBookingService deskBookingService;

    private DeskService deskService;

    private EmployeeService employeeService;
    private TimeSlotService timeSlotService;

    public DeskBookingController(DeskBookingService deskBookingService, DeskService deskService, EmployeeService employeeService, TimeSlotService timeSlotService) {
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
    public String addDeskBookingForm(Model model){
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
    public String addDeskBooking(@Valid DeskBooking deskBooking, BindingResult bindingResult) throws DeskNotAvailableException {
        if (bindingResult.hasErrors()){
            return "DeskBookings/addDeskBooking";
        }else{
            this.deskBookingService.addDeskBooking(deskBooking);
            return "redirect:/web/v1/deskBookings";
        }
    }

    @GetMapping("/view/{id}")
    public String viewDeskBooking(@PathVariable Long id, Model model){
        try{
            DeskBooking deskBooking =  this.deskBookingService.getBookingById(id);
            model.addAttribute("myDeskBooking", deskBooking);
            return "DeskBookings/viewDeskBooking";
        }catch(BookingNotFoundException bookingNotFoundException){
            return "redirect:/web/v1/deskBookings";
        }
    }
    @GetMapping("/edit/{id}")
    public String updateDeskBookingForm(@PathVariable Long id, Model model) throws BookingNotFoundException {
        model.addAttribute("booking", deskBookingService.getBookingById(id));
        return "DeskBookings/editDeskBooking";
    }

    @PostMapping("/{id}")
    public String updateDeskBooking(@PathVariable Long id, DeskBooking booking) throws BookingNotFoundException, DeskNotAvailableException {
        deskBookingService.updateBookingById(id, booking);
        return "redirect:/web/v1/deskBookings";
    }

    @GetMapping("/delete/{id}")
    public String deleteDeskbooking(@PathVariable Long id) throws BookingNotFoundException {
        deskBookingService.deleteBooking(id);
        return "redirect:/web/v1/deskBookings";
    }

    @GetMapping("/mybookings/{employeeId}")
    public String getMyDeskBookings(Model model, Long employeeId) {
        model.addAttribute("myBookings", deskBookingService.getBookingsByEmployeeId(employeeId));
        return "DeskBookings/myDeskBookings";
    }

    @GetMapping("/bookinghistory/{employeeId}")
    public String getMyDeskBookingHistory(Model model, Long employeeId) {
        model.addAttribute("myBookings", deskBookingService.getMyBookingHistory(employeeId));
        return "DeskBookings/myDeskBookingsHistory";
    }

    @GetMapping("/admin")
    public String getAdminDashboard(Model model) {
        model.addAttribute("bookings", deskBookingService.getAllBookings());
        model.addAttribute("desks", deskService.getAllDesks());
        // additional items to display on the admin dashboard eg. All Equipment Bookings & All Room Bookings
        return "DeskBookings/adminDashboard";
    }

}
