package com.itkolleg.bookingsystem.repos;

import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;

import java.util.List;
import java.util.concurrent.ExecutionException;


public interface DBAccessEmployees {
    Employee saveEmployee(Employee employee) throws ExecutionException, InterruptedException, EmployeeAlreadyExistsException;
    List<Employee>getAllEmployees() throws ExecutionException, InterruptedException;

    Employee getEmployeeById(Long id) throws EmployeeNotFoundException, ExecutionException, InterruptedException;

    void deleteEmployeeById(Long id) throws EmployeeDeletionNotPossibleException;

    List<Employee> findEmployeesByNickLikeIgnoreCase(String nick) throws EmployeeNotFoundException, ExecutionException, InterruptedException;

    Employee getEmployeeByEmail(String email);

    Employee getEmployeeByNick(String nick);

}
