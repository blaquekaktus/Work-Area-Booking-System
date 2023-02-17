package com.itkolleg.bookingsystem.repos;

import com.itkolleg.bookingsystem.domains.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeJPARepo extends JpaRepository <Employee, Long>{

    /**
     * @param fname
     * @param lname
     * @return
     */
    //explore use of this method more
    List<Employee> findAllByFnameOrLnameContains(String fname, String lname);

    Employee findEmployeeByFnameOrLnameContaining(String fname, String lname );

    Employee findByNickContains(String nick);
}
