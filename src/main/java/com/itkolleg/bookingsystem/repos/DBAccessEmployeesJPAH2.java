package com.itkolleg.bookingsystem.repos;

import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class DBAccessEmployeesJPAH2 implements DBAccessEmployees{

    private final EmployeesJPAH2 employeesJPAH2;

    public DBAccessEmployeesJPAH2(EmployeesJPAH2 employeesJPAH2){
        this.employeesJPAH2 = employeesJPAH2;
    }


    @Override
    public Employee saveEmployee(Employee employee) throws EmployeeAlreadyExistsException {
        try{
            return this.employeesJPAH2.save(employee);
        } catch(Exception e){
            throw new EmployeeAlreadyExistsException("Mitarbeiter exisitiert bereits!");
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        return this.employeesJPAH2.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) throws EmployeeNotFoundException {
        Optional<Employee> optEmployee = this.employeesJPAH2.findById(id);
        if(optEmployee.isPresent()){
            return optEmployee.get();
        } else {
            throw new EmployeeNotFoundException();
        }
    }

    @Override
    public void deleteEmployeeById(Long id) throws EmployeeDeletionNotPossibleException {
        try{
            this.employeesJPAH2.deleteById(id);
        }catch(Exception e){
            throw new EmployeeDeletionNotPossibleException("Mitarbeiter mit der ID " + id + " konnte nicht gelöscht werden!");
        }
    }

    @Override
    public List<Employee> findEmployeesByNickLikeIgnoreCase(String nick) throws EmployeeNotFoundException, ExecutionException, InterruptedException {
        return this.employeesJPAH2.findEmployeesByNickLikeIgnoreCase("%" + nick + "%");
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        return this.employeesJPAH2.getEmployeeByEmail(email);
    }

    @Override
    public Employee getEmployeeByNick(String nick) {
        return this.employeesJPAH2.getEmployeeByNick(nick);
    }


}
