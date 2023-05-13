package com.itkolleg.bookingsystem.Service;

import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.repos.DBAccessEmployees;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class EmployeeServiceImplementation implements EmployeeService{


    private final DBAccessEmployees dbAccessEmployees;

    /**
     * @param dbAccessEmployees
     */
    public EmployeeServiceImplementation(DBAccessEmployees dbAccessEmployees){
        this.dbAccessEmployees = dbAccessEmployees;
    }


    /**
     * @param employee
     * @return
     */
    @Override
    public Employee addEmployee(Employee employee) throws ExecutionException, InterruptedException, EmployeeAlreadyExistsException {
        return this.dbAccessEmployees.saveEmployee(employee);
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
    public Employee updateEmployeeById(Employee employee) throws EmployeeNotFoundException, ExecutionException, InterruptedException, EmployeeAlreadyExistsException {
        Employee employeeFromDb = this.dbAccessEmployees.getEmployeeById(employee.getId());
        if (employeeFromDb == null) {
            throw new EmployeeNotFoundException();
        }
        employeeFromDb.setFname(employee.getFname());
        employeeFromDb.setLname(employee.getLname());
        employeeFromDb.setNick(employee.getNick());
        employeeFromDb.setPassword(employee.getPassword());
        employeeFromDb.setEmail(employee.getEmail());
        employeeFromDb.setRole(employee.getRole());

        return this.dbAccessEmployees.saveEmployee(employeeFromDb);

    }

    /**
     * @param
     */
    @Override
    public void deleteEmployeeById(Long id) throws EmployeeDeletionNotPossibleException {
       this.dbAccessEmployees.deleteEmployeeById(id);
    }

    @Override
    public List<Employee> getEmployeesWithNickLikeIgnoreCase(String nick) throws ExecutionException, InterruptedException, EmployeeNotFoundException {
        return this.dbAccessEmployees.findEmployeesByNickLikeIgnoreCase(nick);
    }

    @Override
    public Employee getEmployeeByEmail(String email) throws EmployeeNotFoundException {
        return this.dbAccessEmployees.getEmployeeByEmail(email);
    }

    @Override
    public Employee getEmployeeByNick(String nick) {
        return this.dbAccessEmployees.getEmployeeByNick(nick);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = this.dbAccessEmployees.getEmployeeByNick(username);
        if (employee == null) {
            throw new UsernameNotFoundException("Mitarbeiter mit dem nick " + username + "nicht gefunden!");
        }
        return User.builder()
                .username(employee.getNick())
                .password(employee.getPassword())
                .roles(employee.getRole().toString())
                .build();
    }


    /*
    public String getPasswordForEmployee(String nick) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(nick);
        return userDetails.getPassword();
        // Do something with the password
    }
*/
}
