package com.itkolleg.bookingsystem.controller.employee;

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
        return new ModelAndView("employee/allemployees", "employees", allEmployees);
    }

    @GetMapping("/web/insertemployeeform")
    public ModelAndView insertemployeeform(){
        return new ModelAndView("employee/insertemployeeform", "myemployee", new Employee());
    }

    @GetMapping("/web/deleteemployee/{id}")
    public String deleteStudentWithId(@PathVariable Long id, Model model) {
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

    @PostMapping("/web/insertemployee")
    public String insertEmployee(@Valid @ModelAttribute("myemployee") Employee employee,Model model, BindingResult bindingResult) throws ExecutionException, InterruptedException {
        if (bindingResult.hasErrors()) {
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
}
