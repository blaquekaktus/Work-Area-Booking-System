package com.itkolleg.bookingsystem.repos;

import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class DBAccessEmployeesJPAH2 implements DBAccessEmployees{

    private EmployeesJPAH2 employeesJPAH2;

    public DBAccessEmployeesJPAH2(EmployeesJPAH2 employeesJPAH2){
        this.employeesJPAH2 = employeesJPAH2;
    }


    @Override
    public Employee addEmployee(Employee employee) throws EmployeeAlreadyExistsException {
        return this.employeesJPAH2.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return this.employeesJPAH2.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return null;
    }

    @Override
    public Employee updateEmployeeById(Employee employee){
        return null;
    }

    @Override
    public void deleteEmployeeById(Long id) {

    }

    public List<Employee> findAllByNick(String nick){
        return this.employeesJPAH2.findAllByNick(nick);
    }
}
