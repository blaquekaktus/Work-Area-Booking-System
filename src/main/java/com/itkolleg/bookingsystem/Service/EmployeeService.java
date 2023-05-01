package com.itkolleg.bookingsystem.Service;

import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface EmployeeService extends UserDetailsService {
    Employee addEmployee(Employee Employee) throws ExecutionException, InterruptedException, EmployeeAlreadyExistsException;
    List<Employee> getAllEmployees() throws ExecutionException, InterruptedException;

    Employee getEmployeeById(Long id) throws EmployeeNotFoundException, ExecutionException, InterruptedException;

    Employee updateEmployeeById(Employee employee) throws ExecutionException, InterruptedException, EmployeeNotFoundException, EmployeeAlreadyExistsException;

    void deleteEmployeeById(Long id) throws EmployeeDeletionNotPossibleException;

    List<Employee> getEmployeesWithNickLikeIgnoreCase(String nick) throws ExecutionException, InterruptedException, EmployeeNotFoundException;

    Employee getEmployeeByEmail(String email) throws EmployeeNotFoundException;
    //String getPasswordForEmployee(String username);
}
