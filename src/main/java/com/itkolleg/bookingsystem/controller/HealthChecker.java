package com.itkolleg.bookingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class HealthChecker implements HealthIndicator {

    @Autowired
    private DataSource dataSource;

    @Override
    public Health health() {
        try {
            Connection connection = dataSource.getConnection();
            connection.close();
            return Health.up().build();
        } catch (SQLException e) {
            return Health.down().withDetail("Error", e.getMessage()).build();
        }
    }
}