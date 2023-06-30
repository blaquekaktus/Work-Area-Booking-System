package com.itkolleg.bookingsystem.controller;

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
import java.util.Collections;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * This class represents a controller for managing desk bookings within the booking system.
 * It handles operations such as retrieving desk bookings, adding desk bookings, updating desk bookings,
 * and canceling desk bookings.
 *
 * <p>It interacts with the {@link DeskBookingService}, {@link DeskService}, {@link EmployeeService},
 * and {@link TimeSlotService} to perform the necessary operations.
 *
 * <p>The controller is responsible for handling HTTP requests related to desk bookings.
 *
 * <p>Note: This controller is designed to work with the Web interface of the booking system.
 *
 * <p>The endpoints provided by this controller are:
 * <ul>
 *   <li>/web/deskbookings/admin - Retrieves all desk bookings (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/view/{id} - Retrieves the details of a specific desk booking (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/add - Displays the form for adding a desk booking (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/add - Adds a new desk booking based on the submitted form data (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/new/{deskId} - Displays the form for adding a new desk booking for a specific desk (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/new - Adds a new desk booking based on the submitted form data (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/update/{id} - Displays the form for updating a specific desk booking (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/update - Updates a specific desk booking based on the submitted form data (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/cancel/{id} - Displays the form for canceling a specific desk booking ((accessible to admins and operators)</li>
 *   <li>/web/deskbookings/admin/cancel/{id} - Cancels a specific desk booking (accessible to admins and operators)</li>
 *   <li>/web/deskbookings/mydeskbookings - Retrieves the desk bookings for the currently logged-in employee</li>
 *   <li>/web/deskbookings/view/{id} - Retrieves the details of a specific desk booking for the currently logged-in employee</li>
 *   <li>/web/deskbookings/add - Displays the form for adding a desk booking for the currently logged-in employee</li>
 *   <li>/web/deskbookings/add - Adds a new desk booking based on the submitted form data for the currently logged-in employee</li>
 *   <li>/web/deskbookings/update/{id} - Displays the form for updating a specific desk booking for the currently logged-in employee</li>
 *   <li>/web/deskbookings/update - Updates a specific desk booking based on the submitted form data for the currently logged-in employee</li>
 *   <li>/web/deskbookings/deskbookinghistory/{id} - Retrieves the desk booking history for a specific employee</li>
 *   <li>/web/deskbookings/cancel/{id} - Displays the form for cancelling a specific desk booking for the currently logged-in employee</li>
 *   <li>/web/deskbookings/cancel/{id} - Cancels a specific desk booking for the currently logged-in employee</li>
 *   <li>/web/deskbookings/error - Displays the error page</li>
 * </ul>
 *
 * <p>It also includes various model attribute methods to provide data for the views, such as the list of employees,
 * desks, start times, and end times.
 *
 * <p>Note: This controller assumes the use of Spring Framework for building web applications.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */

@Controller
@RequestMapping("/web/deskbookings")
public class DeskBookingWebController {
    private final DeskBookingService deskBookingService;

    private final DeskService deskService;

    private final EmployeeService employeeService;
    private final TimeSlotService timeSlotService;
    private static final Logger logger = LoggerFactory.getLogger(DeskBookingWebController.class);

    /**
     * Constructs a new DeskBookingWebController with the specified services.
     *
     * @param deskBookingService the DeskBookingService to be used
     * @param deskService the DeskService to be used
     * @param employeeService the EmployeeService to be used
     * @param timeSlotService the TimeSlotService to be used
     */
    public DeskBookingWebController(DeskBookingService deskBookingService, DeskService deskService, EmployeeService employeeService, TimeSlotService timeSlotService) {
        this.deskBookingService = deskBookingService;
        this.deskService = deskService;
        this.employeeService = employeeService;
        this.timeSlotService = timeSlotService;
    }

    /**
     * Retrieves a list of all employees to be used as a model attribute.
     *
     * @return A list of all employees.
     * @throws ExecutionException   If an execution exception occurs.
     * @throws InterruptedException If the execution is interrupted.
     */
    @ModelAttribute("employees")
    public List<Employee> getEmployees() throws ExecutionException, InterruptedException {
        return this.employeeService.getAllEmployees();
    }

    /**
     * Retrieves a list of all desks to be used as a model attribute.
     *
     * @return A list of all desks, or an empty list if an error occurs.
     */
    @ModelAttribute("desks")
    public List<Desk> getDesks() {
        try {
            return this.deskService.getAllDesks();
        } catch (Exception e) {
            logger.error("Error occurred while getting all desks: {}", e.getMessage(), e);
            return Collections.emptyList(); // Return an empty list as a default value
        }
    }

    /**
     * Retrieves a list of all start times for time slots to be used as a model attribute.
     *
     * @return A list of all start times.
     * @throws ExecutionException   If an execution exception occurs.
     * @throws InterruptedException If the execution is interrupted.
     */
    @ModelAttribute("startTimes")
    public List<String> getStartTimes() throws ExecutionException, InterruptedException {
        return this.timeSlotService.getAllTimeSlots().stream()
                .map(TimeSlot::getStartTimeAsString)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of all end times for time slots to be used as a model attribute.
     *
     * @return A list of all end times.
     * @throws ExecutionException   If an execution exception occurs.
     * @throws InterruptedException If the execution is interrupted.
     */
    @ModelAttribute("endTimes")
    public List<String> getEndTimes() throws ExecutionException, InterruptedException {
        return this.timeSlotService.getAllTimeSlots().stream()
                .map(TimeSlot::getEndTimeAsString)
                .distinct()
                .collect(Collectors.toList());
    }


    /**
     * Retrieves all desk bookings and adds them as a model attribute for displaying all desk bookings in the admin view.
     *
     * @param model The model to add the desk bookings to.
     * @return The view name for displaying all desk bookings in the admin view.
     */
    @GetMapping("/admin")
    public String getAllDeskBookings(Model model) {
        model.addAttribute("viewAllDeskBookings", this.deskBookingService.getAllBookings());
        return "DeskBookings/Admin/allDeskBookings";
    }

    /**
     * Retrieves a specific desk booking by ID and adds it as a model attribute for viewing in the admin view.
     *
     * @param id    The ID of the desk booking to view.
     * @param model The model to add the desk booking to.
     * @return The view name for viewing the desk booking in the admin view.
     * @throws ResourceNotFoundException If the desk booking with the specified ID is not found.
     */
    @GetMapping("/admin/view/{id}")
    public String viewDeskBooking(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        DeskBooking deskBooking = this.deskBookingService.getBookingById(id);
        model.addAttribute("deskBooking", deskBooking);
        return "DeskBookings/Admin/viewDeskBooking";
    }

    /**
     * Displays the form for adding a new desk booking in the admin view.
     *
     * @param model The model to add the form and error message (if any) to.
     * @return The view name for the add desk booking form in the admin view.
     */
    @GetMapping("/admin/add")
    public String addDeskBookingForm(Model model) {
        model.addAttribute("deskBooking", new DeskBooking());
        if (!model.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", null);
        }
        return "DeskBookings/Admin/addDeskBooking";
    }

    /**
     * Processes the submission of the add desk booking form in the admin view.
     *
     * @param booking             The desk booking object to add.
     * @param bindingResult       The result object for performing data binding and validation.
     * @param employeeId          The ID of the employee for the desk booking.
     * @param deskId              The ID of the desk for the desk booking.
     * @param redirectAttributes  The redirect attributes to add flash attributes for error messages.
     * @return The redirect path after adding the desk booking, or the add desk booking form if validation errors occur.
     */
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


    /**
     * Displays the form for creating a new desk booking with a specific desk ID in the admin view.
     *
     * @param deskId              The ID of the desk for which to create the desk booking.
     * @param model               The model to add the form, default desk ID, and error message (if any) to.
     * @param redirectAttributes  The redirect attributes to retrieve the flash attributes, such as error messages.
     * @return The view name for the new desk booking form in the admin view.
     */
    @GetMapping("/admin/new/{deskId}")
    public String newDeskBookingForm(@PathVariable("deskId") Long deskId, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("deskBooking", new DeskBooking());
        model.addAttribute("defaultDeskId", deskId);
        model.addAttribute("errorMessage", redirectAttributes.getFlashAttributes().get("errorMessage"));
        return "DeskBookings/Admin/newDeskBooking";
    }

    /**
     * Processes the submission of the new desk booking form in the admin view.
     *
     * @param booking             The desk booking object to add.
     * @param bindingResult       The result object for performing data binding and validation.
     * @param employeeId          The ID of the employee for the desk booking.
     * @param deskId              The ID of the desk for the desk booking.
     * @param redirectAttributes  The redirect attributes to add flash attributes for error messages.
     * @return The redirect path after adding the desk booking, or the new desk booking form if validation errors occur.
     */
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

    /**
     * Displays the form for updating a specific desk booking in the admin view.
     *
     * @param id      The ID of the desk booking to update.
     * @param model   The model to add the desk booking, booking date, employees, desks, unique start times, and unique end times to.
     * @return The view name for the update desk booking form in the admin view.
     * @throws ResourceNotFoundException If the desk booking with the specified ID is not found.
     * @throws ExecutionException        If an error occurs while retrieving employees, desks, or time slots.
     * @throws InterruptedException      If the thread is interrupted while retrieving employees, desks, or time slots.
     */
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

    /**

     Updates a desk booking.
     @param booking The desk booking object.
     @param bindingResult The binding result object for validation.
     @param id The ID of the booking.
     @param deskId The ID of the desk.
     @param employeeId The ID of the employee.
     @param date The date of the booking.
     @return The view for updating a desk booking.
     @throws ResourceNotFoundException If the resource is not found.
     @throws DeskNotAvailableException If the desk is not available.
     @throws ExecutionException If an execution error occurs.
     @throws InterruptedException If the execution is interrupted.
     @throws EmployeeNotFoundException If the employee is not found.
     */
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

    /**
     Displays the cancellation form for a desk booking.
     @param id The ID of the booking.
     @param model The model object for rendering the view.
     @return The view for canceling a desk booking.
     @throws ResourceDeletionFailureException If the resource deletion fails.
     @throws ResourceNotFoundException If the resource is not found.
     */
    @GetMapping("/admin/cancel/{id}")
    public String cancelDeskBookingForm(@PathVariable Long id, Model model) throws ResourceDeletionFailureException, ResourceNotFoundException {
        DeskBooking booking = this.deskBookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        return "DeskBookings/Admin/cancelDeskBooking";
    }

    /**

     Cancels a desk booking.
     @param id The ID of the booking.
     @param redirectAttributes The redirect attributes object for adding flash attributes.
     @return The view for canceling a desk booking.
     */
    @PostMapping("/admin/cancel/{id}")
    public String cancelDeskBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            this.deskBookingService.deleteBooking(id);
            return "redirect:/web/deskbookings/admin";
        } catch (ResourceDeletionFailureException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to cancel the desk booking.");
            return "redirect:/web/deskbookings/admin/cancel/" + id;
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found.");
            return "redirect:/web/deskbookings/admin";
        }
    }


    /**
     Retrieves the desk bookings for the currently logged-in employee.
     @param model The model object for rendering the view.
     @param authentication The authentication object for accessing the authenticated user details.
     @return The view for displaying the desk bookings of the employee.
     */
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

    /**
     Displays the details of a desk booking.
     @param id The ID of the booking.
     @param model The model object for rendering the view.
     @return The view for viewing a desk booking.
     @throws ResourceNotFoundException If the resource is not found.
     */
    @GetMapping("/view/{id}")
    public String viewEDeskBooking(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        DeskBooking deskBooking = this.deskBookingService.getBookingById(id);
        // log the deskBooking object
        logger.info("DeskBooking: " + deskBooking);
        model.addAttribute("myDeskBooking", deskBooking);
        return "DeskBookings/viewDeskBooking";
    }

    /**
     Displays the add desk booking form for an employee.
     @param model The model object for rendering the view.
     @return The view for adding a desk booking.
     */
    @GetMapping("/add")
    public String addDeskBookingFormForEmployee(Model model, @RequestParam(value = "deskId", required = false) Long deskId) {
        DeskBooking deskBooking = new DeskBooking();

        if (deskId != null) {
            try {
                Desk desk = deskService.getDeskById(deskId);
                if (desk != null) {
                    deskBooking.setDesk(desk);
                } else {
                    logger.error("No desk found with id: {}", deskId);
                }
            } catch (Exception e) {
                logger.error("Error occurred while getting desk with id: {}", deskId, e);
            }
        }

        model.addAttribute("deskBooking", deskBooking);

        if (!model.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", null);
        }

        return "DeskBookings/Admin/addDeskBooking";
    }


    /**
     Adds a new desk booking.
     @param booking The desk booking object.
     @param bindingResult The binding result object for validation.
     @param employeeId The ID of the employee.
     @param deskId The ID of the desk.
     @param redirectAttributes The redirect attributes object for adding flash attributes.
     @return The view for adding a desk booking.
     */
    @PostMapping("/add")
    public String addEDeskBooking(@ModelAttribute("deskBooking") @Valid DeskBooking booking, BindingResult bindingResult, @RequestParam("employee.id") Long employeeId, @RequestParam("desk.id") Long deskId, RedirectAttributes redirectAttributes) {
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
            return "redirect:/web/deskbookings/mydeskbookings";

        } catch (Exception e) {
            logger.error("Error occurred while booking the desk: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/web/deskbookings/admin/add";
        }
    }

    /**
     Displays the new desk booking form for a specific desk.
     @param deskId The ID of the desk.
     @param model The model object for rendering the view.
     @param redirectAttributes The redirect attributes object for retrieving flash attributes.
     @return The view for creating a new desk booking.
     */
    @GetMapping("/new/{deskId}")
    public String newEDeskBookingForm(@PathVariable("deskId") Long deskId, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("deskBooking", new DeskBooking());
        model.addAttribute("defaultDeskId", deskId);
        model.addAttribute("errorMessage", redirectAttributes.getFlashAttributes().get("errorMessage"));
        return "DeskBookings/newDeskBooking";
    }

    /**
     Creates a new desk booking.
     @param booking The desk booking object.
     @param bindingResult The binding result object for validation.
     @param employeeId The ID of the employee.
     @param deskId The ID of the desk.
     @param redirectAttributes The redirect attributes object for adding flash attributes.
     @return The view for creating a new desk booking.
     */
    @PostMapping("/new")
    public String newEDeskBooking(@ModelAttribute("deskBooking") @Valid DeskBooking booking, BindingResult bindingResult, @RequestParam("employee.id") Long employeeId, @RequestParam("desk.id") Long deskId, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                logger.error("Validation errors: {}", bindingResult.getAllErrors());
                redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred.");
                return "redirect:/web/deskbookings/new";
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
            return "redirect:/web/deskbookings/new";
        }
    }

    /**
     Displays the update desk booking form for a specific booking.
     @param id The ID of the booking.
     @param model The model object for rendering the view.
     @return The view for updating a desk booking.
     @throws ResourceNotFoundException If the resource is not found.
     @throws ExecutionException If an execution error occurs.
     @throws InterruptedException If the execution is interrupted.
     @throws EmployeeNotFoundException If the employee is not found.
     */
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

    /**
     Updates a desk booking.
     @param booking The desk booking object.
     @param bindingResult The binding result object for validation.
     @param id The ID of the booking.
     @param deskId The ID of the desk.
     @param employeeId The ID of the employee.
     @param date The date of the booking.
     @return The view for updating a desk booking.
     @throws ResourceNotFoundException If the resource is not found.
     @throws DeskNotAvailableException If the desk is not available.
     @throws ExecutionException If an execution error occurs.
     @throws InterruptedException If the execution is interrupted.
     @throws EmployeeNotFoundException If the employee is not found.
     */
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

            this.deskBookingService.updateBookingById(booking.getId(), booking);
            return "redirect:/web/deskbookings/mydeskbookings";
        }
    }

    /**
     Retrieves the desk booking history for a specific employee.
     @param model The model object for rendering the view.
     @param id The ID of the employee.
     @return The view for displaying the desk booking history.
     */
    @GetMapping("/deskbookinghistory/{id}")
    public String getMyDeskBookingHistory(Model model, @PathVariable Long id) {
        List<DeskBooking> myBookingHistory = this.deskBookingService.getMyBookingHistory(id);
        model.addAttribute("myBookingHistory", myBookingHistory);
        return "DeskBookings/myDeskBookingsHistory";
    }

    /**
     Displays the cancellation form for a desk booking.
     @param id The ID of the booking.
     @param model The model object for rendering the view.
     @return The view for canceling a desk booking.
     @throws ResourceNotFoundException If the resource is not found.
     */
    @GetMapping("/cancel/{id}")
    public String cancelEDeskBookingForm(@PathVariable Long id, Model model) throws ResourceNotFoundException {
        DeskBooking booking = this.deskBookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        return "DeskBookings/cancelDeskBooking";
    }

    /**
     Cancels a desk booking.
     @param id The ID of the booking.
     @param redirectAttributes The redirect attributes object for adding flash attributes.
     @return The view for canceling a desk booking.
     */
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

    /**
     Displays the error page.
     @return The view for the error page.
     */
    @GetMapping("/error")
    public String getError() {
        return "error";
    }
}
