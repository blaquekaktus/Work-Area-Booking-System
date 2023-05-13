package com.itkolleg.bookingsystem.repos.Employee;
import java.util.List;

@Repository
public interface EmployeeJPARepo extends JpaRepository<Employee, Long> {
    List<Employee>findEmployeesByNameContainsIgnoreCase(String name);
    Employee findByNameIgnoreCase(String name);
    Employee findByNickIgnoreCase(String nick);
    @Query("select a from Employee a")
    Page<Employee> findAllEmployeesByPage(Pageable pageable);
}