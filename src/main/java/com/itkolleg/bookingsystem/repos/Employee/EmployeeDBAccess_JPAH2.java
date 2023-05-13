package com.itkolleg.bookingsystem.repos.Employee;

import java.util.List;
import java.util.Optional;

@Component
public class EmployeeDBAccess_JPAH2 implements EmployeeDBAccess {

    EmployeeJPARepo employeeJPARepo;

    public EmployeeDBAccess_JPAH2(EmployeeJPARepo employeeJPARepo) {
        this.employeeJPARepo = employeeJPARepo;
    }

    @Override
    public Employee addEmployee(Employee employee) {
        return this.employeeJPARepo.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return this.employeeJPARepo.findAll();
    }

    public Page<Employee> getAllEmployeesByPage(Pageable pageable){
        return this.employeeJPARepo.findAllEmployeesByPage(pageable);
    }

    @Override
    public Employee getEmployeeById(Long id) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = this.employeeJPARepo.findById(id);
        if (employeeOptional.isPresent()) {
            return employeeOptional.get();
        } else {
            throw new EmployeeNotFoundException("The Employee with the ID: " + id + " was not found!");
        }
    }

    @Override
    public Employee updateEmployeeById(Long id, Employee updatedEmployee) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = this.employeeJPARepo.findById(id);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setName(updatedEmployee.getName());
            employee.setNick(updatedEmployee.getNick());
            employee.setEmail(updatedEmployee.getEmail());
            employee.setPassword(updatedEmployee.getPassword());
            employee.setRole(updatedEmployee.getRole());
            return this.employeeJPARepo.save(employee);
        } else {
            throw new EmployeeNotFoundException("The Employee with the ID: " + id + " was not found!");
        }
    }

    public Employee updateEmployee(Employee updatedEmployee) throws EmployeeNotFoundException{
        return this.employeeJPARepo.save(updatedEmployee);
    }


    @Override
    public void deleteEmployeeById(Long id) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = this.employeeJPARepo.findById(id);
        if (employeeOptional.isPresent()) {
            this.employeeJPARepo.deleteById(id);
        } else {
            throw new EmployeeNotFoundException("The Employee with the ID: " + id + " was not found!");
        }
    }
}