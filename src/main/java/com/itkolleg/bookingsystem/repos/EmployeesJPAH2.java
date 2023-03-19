package com.itkolleg.bookingsystem.repos;

import com.itkolleg.bookingsystem.domains.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeesJPAH2 extends JpaRepository<Employee, Long> {
    public List<Employee> findAllByNick(String nick);

}
