package com.itkolleg.bookingsystem;

import com.itkolleg.bookingsystem.repos.DBAccessEmployees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

@SpringBootApplication
public class WABSRunner {

    @Autowired
    DBAccessEmployees dbAccessEmployees;


    public static void main(String[] args) {
        SpringApplication.run(WABSRunner.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.dbAccessEmployees.
    }
}

