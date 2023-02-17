package com.itkolleg.bookingsystem.repos;

import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.EmployeeNotFoundException;

import java.util.List;
import java.util.concurrent.ExecutionException;


public interface DBAccessEmployees {
    Employee addEmployee(Employee employee) throws ExecutionException, InterruptedException, EmployeeAlreadyExistsException;
    List<Employee>getAllEmployees() throws ExecutionException, InterruptedException;

    Employee getEmployeeById(Long id) throws EmployeeNotFoundException, ExecutionException, InterruptedException;

    Employee updateEmployeeById(Employee employee) throws EmployeeNotFoundException, ExecutionException, InterruptedException;

    void deleteEmployeeById(Long id) throws EmployeeDeletionNotPossibleException;
}
