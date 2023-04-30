package com.itkolleg.bookingsystem;

import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Role;
import com.itkolleg.bookingsystem.repos.DBAccessEmployees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class WABSRunner implements ApplicationRunner{

    //
    @Autowired
    DBAccessEmployees dbAccessEmployees;


    public static void main(String[] args) {
        SpringApplication.run(WABSRunner.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
            this.dbAccessEmployees.addEmployee(new Employee("Marcel", "Schranz", "Marsa", "marcel-schranz@hotmail.com", "Testitest", Role.ADMIN));
    }
}

