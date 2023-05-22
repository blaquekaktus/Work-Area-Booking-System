package com.itkolleg.bookingsystem;

import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Role;
import com.itkolleg.bookingsystem.repos.Employee.EmployeeDBAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories
public class WABSRunner implements ApplicationRunner {

    //
    @Autowired
    EmployeeDBAccess employeeDBAccess;


    public static void main(String[] args) {
        SpringApplication.run(WABSRunner.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Employee admin = this.employeeDBAccess.saveEmployee(new Employee("Marcel", "Schranz", "admin", "marcel-schranz@hotmail.com", "password", Role.ROLE_ADMIN));
        Employee operator = this.employeeDBAccess.saveEmployee(new Employee("Patrick", "Bayr", "operator", "bayr@hotmail.com", "password", Role.ROLE_OPERATOR));
        Employee pemployee = this.employeeDBAccess.saveEmployee(new Employee("Manuel", "Payr", "nemployee", "bayr@hotmail.com", "password", Role.ROLE_N_EMPLOYEE));
        Employee nemployee = this.employeeDBAccess.saveEmployee(new Employee("Sonja", "Lechner", "pemployee", "bayr@hotmail.com", "password", Role.ROLE_P_EMPLOYEE));
    }
}

