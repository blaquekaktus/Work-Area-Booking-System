package com.itkolleg.bookingsystem;

import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Role;
import com.itkolleg.bookingsystem.repos.DBAccessEmployees;
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
    DBAccessEmployees dbAccessEmployees;


    public static void main(String[] args) {
        SpringApplication.run(WABSRunner.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Employee admin = this.dbAccessEmployees.saveEmployee(new Employee("Marcel", "Schranz", "admin", "marcel-schranz@hotmail.com", "password", Role.ADMIN));
        Employee operator = this.dbAccessEmployees.saveEmployee(new Employee("Patrick", "Bayr", "operator", "bayr@hotmail.com", "password", Role.OPERATOR));
        Employee pemployee = this.dbAccessEmployees.saveEmployee(new Employee("Manuel", "Payer", "pemployee", "m.payer@hotmail.com", "password", Role.P_EMPLOYEE));
        Employee nemployee = this.dbAccessEmployees.saveEmployee(new Employee("Sonja", "Lechner", "nemployee", "lechner@hotmail.com", "password", Role.N_EMPLOYEE));
    }
}

