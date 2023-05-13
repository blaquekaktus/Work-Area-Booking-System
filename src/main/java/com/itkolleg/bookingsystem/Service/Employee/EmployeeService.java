package com.itkolleg.bookingsystem.Service.Employee;
import java.util.List;

public interface EmployeeService {
    Employee addEmployee(Employee employee);
    List<Employee> getAllEmployees();

    Page<Employee> getAllEmployeesByPage(Pageable pageable);
    Employee getEmployeeById(Long id)throws EmployeeNotFoundException;
    Employee updateEmployeeById(Long id, Employee employee) throws EmployeeNotFoundException;
    Employee updateEmployee(Employee employee) throws EmployeeNotFoundException;
    void deleteEmployeeById(Long id) throws EmployeeNotFoundException;
}