package com.itkolleg.bookingsystem.Service;

import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.repos.DBAccessEmployees;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class EmployeeServiceImplementation implements EmployeeService{

    private DBAccessEmployees dbAccessEmployees;

    /**
     * @param dbAccessEmployees
     */
    public EmployeeServiceImplementation(DBAccessEmployees dbAccessEmployees){
        this.dbAccessEmployees = dbAccessEmployees;
    }
    /**
     * @param Employee
     * @return
     */
    @Override
    public Employee addEmployee(Employee Employee) throws ExecutionException, InterruptedException, EmployeeAlreadyExistsException {
        return this.dbAccessEmployees.addEmployee(Employee);
    }

    /**
     * @return
     */
    @Override
    public List<Employee> getAllEmployees() throws ExecutionException, InterruptedException {
        return this.dbAccessEmployees.getAllEmployees();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Employee getEmployeeById(Long id) throws EmployeeNotFoundException, ExecutionException, InterruptedException {
        return this.dbAccessEmployees.getEmployeeById(id);
    }

    /**
     * @param
     * @return
     */
    @Override
    public Employee updateEmployeeById(Employee employee) throws EmployeeNotFoundException, ExecutionException, InterruptedException {
        return this.dbAccessEmployees.updateEmployeeById(employee);
    }

    /**
     * @param
     */
    @Override
    public void deleteEmployeeById(Long id) throws EmployeeDeletionNotPossibleException {
       this.dbAccessEmployees.deleteEmployeeById(id);
    }
}
