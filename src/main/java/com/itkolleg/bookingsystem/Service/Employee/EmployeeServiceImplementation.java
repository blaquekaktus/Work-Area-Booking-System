package com.itkolleg.bookingsystem.Service.Employee;

import java.util.List;

@Service
public class EmployeeServiceImplementation implements EmployeeService {

    private final EmployeeDBAccess employeeDBAccess;

    public EmployeeServiceImplementation(EmployeeDBAccess employeeDBAccess) {
        this.employeeDBAccess = employeeDBAccess;
    }

    @Override
    public Employee addEmployee(Employee employee) {
        return this.employeeDBAccess.addEmployee(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return this.employeeDBAccess.getAllEmployees();
    }

    public Page<Employee> getAllEmployeesByPage(Pageable pageable){
        return this.employeeDBAccess.getAllEmployeesByPage(pageable);
    }
    @Override
    public Employee getEmployeeById(Long id) throws EmployeeNotFoundException {
        return this.employeeDBAccess.getEmployeeById(id);
    }

    @Override
    public Employee updateEmployeeById(Long id, Employee employee) throws EmployeeNotFoundException {
        return this.employeeDBAccess.updateEmployeeById(id, employee);
    }

    public Employee updateEmployee(Employee employee) throws EmployeeNotFoundException{
        return this.employeeDBAccess.updateEmployee(employee);
    }
    @Override
    public void deleteEmployeeById(Long id) throws EmployeeNotFoundException {
        this.employeeDBAccess.deleteEmployeeById(id);
    }

}