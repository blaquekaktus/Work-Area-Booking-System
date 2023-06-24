package com.itkolleg.bookingsystem.controller.employee;

import com.itkolleg.bookingsystem.Service.Employee.EmployeeService;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class EmployeeWebController {

    EmployeeService employeeService;

    public EmployeeWebController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
/*
    @GetMapping("/web/allemployees")
    public ModelAndView allemployees() throws ExecutionException, InterruptedException {
        List<Employee> allEmployees = employeeService.getAllEmployees();
        return new ModelAndView("employee/allemployees", "employees", allEmployees);
    } */

    @GetMapping("/web/allemployees")
    public ModelAndView allemployees() throws ExecutionException, InterruptedException {
        List<Employee> allEmployees = employeeService.getAllEmployees();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("employee/allemployees");
        modelAndView.addObject("employees", allEmployees);
        return modelAndView;
    }


    @GetMapping("/web/admin-start")
    public ModelAndView home() throws ExecutionException, InterruptedException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("employee/admin-start");
        return modelAndView;
    }



    @GetMapping("/web/employeeswithnick")
    public String searchEmployees() throws ExecutionException, InterruptedException, EmployeeNotFoundException {
        return "employee/employeeswithnick";
    }

    @PostMapping("/web/employeeswithnick")
    public String searchEmployeesByNickname(@RequestParam("nickname") String nickname, Model model) throws ExecutionException, InterruptedException, EmployeeNotFoundException {
        List<Employee> employees = employeeService.getEmployeesWithNickLikeIgnoreCase(nickname);
        model.addAttribute("employeesnick", employees);
        return "employee/employeeswithnick";
    }



    @GetMapping("/web/deleteemployee/{id}")
    public String deleteemployeewithid(@PathVariable Long id, Model model) {
        try {
            this.employeeService.deleteEmployeeById(id);
            return "redirect:/web/allemployees";
        } catch (EmployeeDeletionNotPossibleException e)
        {
            model.addAttribute("errortitle", "Mitarbeiter-Löschen schlägt fehl!");
            model.addAttribute("errormessage", e.getMessage());
            return "myerrorspage";
        }
    }

    @GetMapping("/web/insertemployeeform")
    public ModelAndView insertemployeeform(){
        return new ModelAndView("employee/insertemployeeform", "myemployee", new Employee());
    }

    @PostMapping("/web/insertemployee")
    public String insertEmployee(@Valid @ModelAttribute("myemployee") Employee employee, Model model, BindingResult bindingResult) throws ExecutionException, InterruptedException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
            return "employee/insertemployeeform";
        } else {
            try {
                this.employeeService.addEmployee(employee);
                return "redirect:/web/allemployees";
            } catch (EmployeeAlreadyExistsException e) {
                model.addAttribute("errortitle", "Mitarbeiter kann nicht eingefügt werden!");
                model.addAttribute("errormessage", e.getMessage());
                return "myerrorspage";
            }
        }
    }


    @GetMapping("/web/editemployee/{id}")
    public String editEmployeeForm(@PathVariable Long id, Model model) throws EmployeeNotFoundException, ExecutionException, InterruptedException {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {

            model.addAttribute("employee", employee);

            return "employee/editemployeeform";
        } else {
            throw new EmployeeNotFoundException();
        }
    }
    @PostMapping("/web/editemployee")
    public String editEmployee(@Valid @ModelAttribute("employee") Employee updatedEmployee, Model model, BindingResult bindingResult) throws EmployeeNotFoundException {
        if (bindingResult.hasErrors()) {
            return "employee/editemployeeform";
        } else {
            try {
                this.employeeService.updateEmployeeById(updatedEmployee);
                return "redirect:/web/allemployees";
            } catch (Exception e) {
                model.addAttribute("errortitle", "Mitarbeiter bearbeiten fehlgeschlagen!");
                model.addAttribute("errormessage", e.getMessage());
                return "myerrorspage";
            }
        }
    }




}
