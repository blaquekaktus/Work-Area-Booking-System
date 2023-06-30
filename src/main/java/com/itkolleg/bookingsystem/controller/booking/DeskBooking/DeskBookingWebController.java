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

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.LocalDate;
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


    public DeskBookingWebController(DeskBookingService deskBookingService, DeskService deskService, EmployeeService employeeService, TimeSlotService timeSlotService) {
        this.deskBookingService = deskBookingService;
        this.deskService = deskService;
        this.employeeService = employeeService;
        this.timeSlotService = timeSlotService;
    }

    @ModelAttribute("employees")
    public List<Employee> getEmployees() throws ExecutionException, InterruptedException {
        return this.employeeService.getAllEmployees();
    }

    @ModelAttribute("desks")
    public List<Desk> getDesks() throws ExecutionException, InterruptedException {
        return this.deskService.getAllDesks();
    }

    @ModelAttribute("startTimes")
    public List<String> getStartTimes() throws ExecutionException, InterruptedException {
        return this.timeSlotService.getAllTimeSlots().stream()
                .map(TimeSlot::getStartTimeAsString)
                .distinct()
                .collect(Collectors.toList());
    }

    @ModelAttribute("endTimes")
    public List<String> getEndTimes() throws ExecutionException, InterruptedException {
        return this.timeSlotService.getAllTimeSlots().stream()
                .map(TimeSlot::getEndTimeAsString)
                .distinct()
                .collect(Collectors.toList());
    }

    @GetMapping("/admin")
    public String getAllDeskBookings(Model model) {
        model.addAttribute("viewAllDeskBookings", this.deskBookingService.getAllBookings());
        return "DeskBookings/Admin/allDeskBookings";
    }

    @GetMapping("/admin/view/{id}")
    public String viewDeskBooking(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        DeskBooking deskBooking = this.deskBookingService.getBookingById(id);
        model.addAttribute("deskBooking", deskBooking);
        return "DeskBookings/Admin/viewDeskBooking";
    }

    @GetMapping("/admin/add")
    public String addDeskBookingForm(Model model) {
        model.addAttribute("deskBooking", new DeskBooking());
        if (!model.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", null);
        }
        return "DeskBookings/Admin/addDeskBooking";
    }

    @PostMapping("/admin/add")
    public String addDeskBooking(@ModelAttribute("deskBooking") @Valid DeskBooking booking, BindingResult bindingResult, @RequestParam("employee.id") Long employeeId, @RequestParam("desk.id") Long deskId, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                logger.error("Validation errors: {}", bindingResult.getAllErrors());
                redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred.");
                return "redirect:/web/deskbookings/admin/add";
            }

            Desk desk = deskService.getDeskById(deskId);
            Employee employee = employeeService.getEmployeeById(employeeId);
            booking.setDesk(desk);
            booking.setEmployee(employee);

            this.deskBookingService.addDeskBooking(booking);
            return "redirect:/web/deskbookings/admin";
        } catch (Exception e) {
            logger.error("Error occurred while booking the desk: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/web/deskbookings/admin/add";
        }
    }

    @GetMapping("/admin/new/{deskId}")
    public String newDeskBookingForm(@PathVariable("deskId") Long deskId, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("deskBooking", new DeskBooking());
        model.addAttribute("defaultDeskId", deskId);
        model.addAttribute("errorMessage", redirectAttributes.getFlashAttributes().get("errorMessage"));
        return "DeskBookings/Admin/newDeskBooking";
    }

    @PostMapping("/admin/new")
    public String newDeskBooking(@ModelAttribute("deskBooking") @Valid DeskBooking booking, BindingResult bindingResult, @RequestParam("employee.id") Long employeeId, @RequestParam("desk.id") Long deskId, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                logger.error("Validation errors: {}", bindingResult.getAllErrors());
                redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred.");
                return "redirect:/web/deskbookings/admin/new";
            }

            Desk desk = deskService.getDeskById(deskId);
            Employee employee = employeeService.getEmployeeById(employeeId);
            booking.setDesk(desk);
            booking.setEmployee(employee);

            this.deskBookingService.addDeskBooking(booking);
            return "redirect:/web/deskbookings/admin";
        } catch (Exception e) {
            logger.error("Error occurred while booking the desk: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/web/deskbookings/admin/new";
        }
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

        // Convert the LocalDate to java.util.Date for the Thymeleaf template
        Date bookingDate = java.sql.Date.valueOf(booking.getDate());
        model.addAttribute("bookingDate", bookingDate);

        //Add booking, bookingDate, employee, desks, unique booking start and end times to the model
        model.addAttribute("booking", booking);
        model.addAttribute("bookingDate", bookingDate); // Add this line
        model.addAttribute("employees", employees);
        model.addAttribute("desks", desks);
        model.addAttribute("uniqueStartTimes", uniqueStartTimes);
        model.addAttribute("uniqueEndTimes", uniqueEndTimes);
        return "DeskBookings/Admin/updateDeskBooking";
    }


    @PostMapping("/admin/update")
    public String updateDeskBooking(@Valid DeskBooking booking, BindingResult bindingResult, @RequestParam("id") Long id, @RequestParam("desk.id") Long deskId, @RequestParam("employee.id") Long employeeId, @RequestParam("date") String date) throws ResourceNotFoundException, DeskNotAvailableException, ExecutionException, InterruptedException, EmployeeNotFoundException {
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

            // Convert date String back to LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, formatter);
            booking.setDate(localDate);
            booking.setDesk(desk);
            booking.setEmployee(employee);
            booking.setId(id);
            //booking.setCreatedOn(LocalDateTime.now());

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
    public String cancelDeskBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            this.deskBookingService.deleteBooking(id);
        } catch (ResourceDeletionFailureException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to cancel the desk booking.");
            return "redirect:/web/deskbookings/admin/cancel/" + id;
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found.");
            return "redirect:/web/deskbookings/admin";
        }

        return "redirect:/web/deskbookings/admin";
    }

    @GetMapping("/mydeskbookings")
    public String getBookings(Model model, Authentication authentication) {
        String username = authentication.getName();
        Employee employee = this.employeeService.findByNick(username);
        Long employeeId = employee.getId();

        // fetch bookings related to this employee
        List<DeskBooking> myBookings = this.deskBookingService.getBookingsByEmployeeId(employeeId);

        // add the bookings to the model
        model.addAttribute("myDeskBookings", myBookings);
        return "DeskBookings/myDeskBookings";
    }

    @GetMapping("/view/{id}")
    public String viewEDeskBooking(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        DeskBooking deskBooking = this.deskBookingService.getBookingById(id);
        // log the deskBooking object
        logger.info("DeskBooking: " + deskBooking);
        model.addAttribute("myDeskBooking", deskBooking);
        return "DeskBookings/viewDeskBooking";
    }

    @GetMapping("/add")
    public String addDeskBookingFormForEmployee(Model model) {
        model.addAttribute("deskBooking", new DeskBooking());
        return "DeskBookings/addDeskBooking";
    }

    @PostMapping("/add")
    public String addEDeskBooking(@ModelAttribute("deskBooking") @Valid DeskBooking booking, BindingResult bindingResult, @RequestParam("employee.id") Long employeeId, @RequestParam("desk.id") Long deskId, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                logger.error("Validation errors: {}", bindingResult.getAllErrors());
                redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred.");
                return "redirect:/web/deskbookings/add";
            }

            Desk desk = deskService.getDeskById(deskId);
            Employee employee = employeeService.getEmployeeById(employeeId);
            booking.setDesk(desk);
            booking.setEmployee(employee);

            this.deskBookingService.addDeskBooking(booking);
            return "redirect:/web/deskbookings/mydeskbookings";

        } catch (Exception e) {
            logger.error("Error occurred while booking the desk: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/web/deskbookings/add";
        }
    }

    @GetMapping("/update/{id}")
    public String updateEDeskBookingForm(@PathVariable Long id, Model model) throws ResourceNotFoundException, ExecutionException, InterruptedException, EmployeeNotFoundException {
        DeskBooking booking = this.deskBookingService.getBookingById(id);

        if (booking == null) {
            // Log an error message indicating that the booking was not found
            logger.error("Booking with ID {} not found.", id);
            return "redirect:/error";
        }

        // Get the eDesks and TimesSlots after checking that the booking is not null
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

        // Convert the LocalDate to java.util.Date for the Thymeleaf template
        Date bookingDate = java.sql.Date.valueOf(booking.getDate());
        model.addAttribute("bookingDate", bookingDate);

        //Add booking, bookingDate, employee, desks, unique booking start and end times to the model
        model.addAttribute("booking", booking);
        model.addAttribute("bookingDate", bookingDate); // Add this line
        model.addAttribute("desks", desks);
        model.addAttribute("uniqueStartTimes", uniqueStartTimes);
        model.addAttribute("uniqueEndTimes", uniqueEndTimes);
        return "DeskBookings/updateDeskBooking";
    }


    @PostMapping("/update")
    public String updateEDeskBooking(@Valid DeskBooking booking, BindingResult bindingResult, @RequestParam("id") Long id, @RequestParam("desk.id") Long deskId, @RequestParam("employee.id") Long employeeId, @RequestParam("date") String date) throws ResourceNotFoundException, DeskNotAvailableException, ExecutionException, InterruptedException, EmployeeNotFoundException {
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

            // Convert date String back to LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, formatter);
            booking.setDate(localDate);
            booking.setDesk(desk);
            booking.setEmployee(employee);
            booking.setId(id);
            //booking.setCreatedOn(LocalDateTime.now());

            this.deskBookingService.updateBookingById(booking.getId(), booking);
            return "redirect:/web/deskbookings/admin";
        }
    }

    @GetMapping("/deskbookinghistory/{id}")
    public String getMyDeskBookingHistory(Model model, @PathVariable Long id) {
        List<DeskBooking> myBookingHistory = this.deskBookingService.getMyBookingHistory(id);
        model.addAttribute("myBookingHistory", myBookingHistory);
        return "DeskBookings/myDeskBookingsHistory";
    }

    @GetMapping("/cancel/{id}")
    public String cancelEDeskBookingForm(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        DeskBooking booking = this.deskBookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        return "DeskBookings/cancelDeskBooking";
    }

    @PostMapping("/cancel/{id}")
    public String cancelEDeskBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            this.deskBookingService.deleteBooking(id);
        } catch (ResourceDeletionFailureException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to cancel the desk booking.");
            return "redirect:/web/deskbookings/cancel/" + id;
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found.");
            return "redirect:/web/deskbookings/mydeskbookings";
        }

        return "redirect:/web/deskbookings/mydeskbookings";
    }

    @GetMapping("/error")
    public String getError() {
        return "error";
    }
}
