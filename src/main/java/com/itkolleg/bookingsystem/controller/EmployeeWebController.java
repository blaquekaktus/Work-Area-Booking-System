package com.itkolleg.bookingsystem.controller;

import com.itkolleg.bookingsystem.Service.EmployeeService;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeDeletionNotPossibleException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class EmployeeWebController {

    EmployeeService employeeService;

    public EmployeeWebController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/web/allemployees")
    public ModelAndView allemployees() throws ExecutionException, InterruptedException {
        List<Employee> allEmployees = employeeService.getAllEmployees();
        return new ModelAndView("allemployees", "employees", allEmployees);
    }

    @GetMapping("/web/insertemployeeform")
    public ModelAndView insertstudentform() {
        return new ModelAndView("insertemployeeform", "myemployee", new Employee());
    }

    @GetMapping("/web/deleteemployee/{id}")
    public String deleteStudentWithId(@PathVariable Long id, Model model) {
        try {
            this.employeeService.deleteEmployeeById(id);
            return "redirect:/web/allstudents";
        } catch (EmployeeDeletionNotPossibleException e)
        {
            model.addAttribute("errortitle", "Mitarbeiter-Löschen schlägt fehl!");
            model.addAttribute("errormessage", e.getMessage());
            return "myerrorspage";
        }
    }

    @PostMapping("/web/insertemployee")
    public String insertEmployee(@Valid @ModelAttribute("myemployee") Employee employee, BindingResult bindingResult) throws EmployeeAlreadyExistsException, ExecutionException, InterruptedException {
        if (bindingResult.hasErrors()) {
            return "/insertemployeeform";
        } else {

            this.employeeService.addEmployee(employee);
            return "redirect:/web/allemployees";
        }
    }
}
