package com.itkolleg.bookingsystem.repos;

import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DBAccessEmployeesJPAH2 implements DBAccessEmployees{
    @Override
    public Employee addEmployee(Employee employee) throws ExecutionException, InterruptedException, EmployeeAlreadyExistsException {
        return null;
    }

    @Override
    public List<Employee> getAllEmployees() throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public Employee getEmployeeById(Long id) throws EmployeeNotFoundException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public Employee updateEmployeeById(Employee employee) throws EmployeeNotFoundException, ExecutionException, InterruptedException, EmployeeNotFoundException {
        return null;
    }

    @Override
    public void deleteEmployeeById(Long id) throws EmployeeDeletionNotPossibleException {

    }
}
