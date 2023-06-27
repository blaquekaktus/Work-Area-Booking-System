package com.itkolleg.bookingsystem.controller.employee;

//import com.itkolleg.bookingsystem.Service.Employee.EmployeeService;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.service.Employee.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/web/employees")
public class EmployeeWebController {

    EmployeeService employeeService;

    public EmployeeWebController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping("/allemployees")
    public ModelAndView allemployees() throws ExecutionException, InterruptedException {
        List<Employee> allEmployees = employeeService.getAllEmployees();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("employee/allemployees");
        modelAndView.addObject("employees", allEmployees);
        return modelAndView;
    }

    @GetMapping("/admin-start")
    public ModelAndView home() throws ExecutionException, InterruptedException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("employee/admin-start");
        return modelAndView;
    }

    @GetMapping("/start")
    public ModelAndView homeUser() throws ExecutionException, InterruptedException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("employee/start");
        return modelAndView;
    }

    @GetMapping("/employeeswithnick")
    public String searchEmployees() throws ExecutionException, InterruptedException, EmployeeNotFoundException {
        return "employee/employeeswithnick";
    }

    @PostMapping("/employeeswithnick")
    public String searchEmployeesByNickname(@RequestParam("nickname") String nickname, Model model) throws ExecutionException, InterruptedException, EmployeeNotFoundException {
        List<Employee> employees = employeeService.getEmployeesWithNickLikeIgnoreCase(nickname);
        model.addAttribute("employeesnick", employees);
        return "employee/employeeswithnick";
    }

    @GetMapping("/deleteemployee/{id}")
    public String deleteemployeewithid(@PathVariable Long id, Model model) {
        try {
            this.employeeService.deleteEmployeeById(id);
            return "redirect:/web/employees/allemployees";
        } catch (EmployeeDeletionNotPossibleException e)
        {
            System.out.println(e.getMessage());
            model.addAttribute("errortitle", "Mitarbeiter-Löschen schlägt fehl!");
            model.addAttribute("errormessage", e.getMessage());
            return "myerrorspage";
        }
    }

    @GetMapping("/insertemployeeform")
    public ModelAndView insertemployeeform(){
        return new ModelAndView("employee/insertemployeeform", "myemployee", new Employee());
    }

    @PostMapping("/insertemployee")
    public String insertEmployee(@Valid @ModelAttribute("myemployee") Employee employee, BindingResult bindingResult, Model model) throws ExecutionException, InterruptedException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
            return "employee/insertemployeeform";
        } else {
            try {
                this.employeeService.addEmployee(employee);
                return "redirect:/web/employees/allemployees";
            } catch (EmployeeAlreadyExistsException e) {
                System.out.println(e.getMessage());
                model.addAttribute("errortitle", "Mitarbeiter kann nicht eingefügt werden!");
                model.addAttribute("errormessage", e.getMessage());
                return "myerrorspage";
            }
        }
    }

    @GetMapping("/editemployee/{id}")
    public String editEmployeeForm(@PathVariable Long id, Model model) throws EmployeeNotFoundException, ExecutionException, InterruptedException {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            model.addAttribute("employee", employee);
            return "employee/editemployeeform";
        } else {
            throw new EmployeeNotFoundException("Mitarbeiter nicht gefunden");
        }
    }

    @PostMapping("/editemployee")
    public String editEmployee(@Valid @ModelAttribute("employee") Employee updatedEmployee, BindingResult bindingResult, Model model) throws EmployeeNotFoundException {
        System.out.println("Validierung fehlgeschlagen: " + bindingResult.getAllErrors());
        if (bindingResult.hasErrors()) {
            System.out.println("Validierung fehlgeschlagen: " + bindingResult.getAllErrors());
            return "employee/editemployeeform";
        } else {
            try {
                this.employeeService.updateEmployeeById(updatedEmployee);
                return "redirect:/web/employees/allemployees";
            } catch (Exception e) {
                System.out.println(e.getMessage());
                model.addAttribute("errortitle", "Mitarbeiter bearbeiten fehlgeschlagen!");
                model.addAttribute("errormessage", e.getMessage());
                return "error";
            }
        }
    }
}
