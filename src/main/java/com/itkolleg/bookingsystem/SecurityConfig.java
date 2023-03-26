package com.itkolleg.bookingsystem;

import com.itkolleg.bookingsystem.Service.EmployeeService;
import com.itkolleg.bookingsystem.Service.EmployeeServiceImplementation;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Konfigurationsklasse für die Sicherheitseinstellungen.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final EmployeeService employeeService;

    public SecurityConfig(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }




    /**
     * Konfiguriert die HttpSecurity-Einstellungen für die Anforderungsautorisierung.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN") // Zugriff nur für Benutzer mit der Rolle "ADMIN" auf "/admin/**"
                .antMatchers("/user/**").hasAnyRole("ADMIN", "USER") // Zugriff für Benutzer mit der Rolle "ADMIN" oder "USER" auf "/user/**"
                .anyRequest().authenticated() // Authentifizierung für alle anderen Anfragen erforderlich
                .and()
                .formLogin() // Verwenden Sie das Standard-Login-Formular von Spring Security
                .loginPage("/login")
                .defaultSuccessUrl("/authenticated")
                .permitAll()
                .and()
                .logout() // Verarbeitung der Standardabmeldung von Spring Security
                .permitAll();
    }


}
