package com.itkolleg.bookingsystem.config;

import com.itkolleg.bookingsystem.service.Desk.DeskService;
import com.itkolleg.bookingsystem.service.DeskBooking.DeskBookingService;
import com.itkolleg.bookingsystem.service.Employee.EmployeeService;
import com.itkolleg.bookingsystem.domains.Employee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Konfigurationsklasse für die Sicherheitseinstellungen.
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final EmployeeService employeeService;
    private final DeskService deskService;
    private final DeskBookingService deskBookingService;


    public WebSecurityConfig(EmployeeService employeeService, DeskService deskService, DeskBookingService deskBookingService) {
        this.employeeService = employeeService;
        this.deskService = deskService;
        this.deskBookingService = deskBookingService;
    }


    /**
     * Bean zum Verschlüsseln des Passworts mit bcrypt-Algorithmus.
     *
     * @return das BCryptPasswordEncoder-Objekt.
     */
    @Bean
    public BCryptPasswordEncoder bCryptpasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Konfiguriert die Sicherheitseinstellungen für die Http-Requests.
     *
     * @param http Die HttpSecurity-Instanz.
     * @return die SecurityFilterChain-Instanz.
     * @throws Exception wenn Fehler bei der Konfiguration auftreten.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .contentTypeOptions()
                .disable()
                .and()
                .authorizeHttpRequests(authConfig -> {
                    authConfig.requestMatchers(HttpMethod.GET, "/web/**", "/web/*", "/web/login", "/web/hello", "/error", "/web/login-error", "/web/logout", "/css/**").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/web/**","/web/login").permitAll();
                    /*authConfig.requestMatchers(HttpMethod.GET, "/web/allemployees").hasRole("USER");
                    authConfig.requestMatchers(HttpMethod.GET, "/web/**", "/web/allemployees", "/web/login", "/web/hello", "/error", "/web/login-error", "/web/logout", "/static/**").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/web/**", "/web/login").permitAll();*/
     /*authConfig.requestMatchers(HttpMethod.GET, "/web/allemployees").hasRole("USER");
>>>>>>> origin/main
     authConfig.requestMatchers(HttpMethod.GET, "/admin").hasRole("ADMIN");
     authConfig.requestMatchers(HttpMethod.GET, "/operator").hasRole("OPERATOR");
     authConfig.requestMatchers(HttpMethod.GET, "/users").hasAnyRole("DEVELOPER");
<<<<<<< HEAD
     authConfig.requestMatchers(HttpMethod.GET, "/authorities").hasAnyRole("DEVELOPER");
     //authConfig.requestMatchers(HttpMethod.GET,  "/web/v1/desks", "/web/v1/desks/add","/web/v1/desks/update/{id}","/web/v1/desks/view/{id}","/web/v1/desks/delete/{id}").permitAll();
     //authConfig.requestMatchers(HttpMethod.POST, "/web/v1/desks/add","/web/v1/desks/update").permitAll();
     //authConfig.requestMatchers(HttpMethod.GET,  "/web/v1/deskBookings", "/web/v1/deskBookings/add","/web/v1/deskBookings/view/{id}","/web/v1/deskBookings/update/{id}","/web/v1/desks/delete/{id}").permitAll();
     //authConfig.requestMatchers(HttpMethod.POST, "/web/v1/deskBookings/add","/web/v1/deskBookings/update").permitAll();

         authConfig.anyRequest().authenticated();
=======
     authConfig.requestMatchers(HttpMethod.GET, "/authorities").hasAnyRole("DEVELOPER");*/
                    authConfig.anyRequest().authenticated();
                })
                .formLogin(login -> {
                            login.loginPage("/web/login");
                            login.defaultSuccessUrl("/web/hello", true);
                            login.failureUrl("/web/login-error");
                        }
                )
                .logout(logout -> {
                    logout.logoutRequestMatcher(new AntPathRequestMatcher("/web/logout"));
                    logout.logoutSuccessUrl("/web/login");
                    logout.deleteCookies("JSESSIONID");
                    logout.invalidateHttpSession(true);
                });
        return http.build();
    }

    /**
     * Bean für den AuthenticationManager.
     *
     * @param httpSecurity          Die HttpSecurity-Instanz.
     * @param userDetailsService    Das UserDetailsService-Objekt.
     * @param bCryptPasswordEncoder Das BCryptPasswordEncoder-Objekt.
     * @return die AuthenticationManager-Instanz.
     * @throws Exception wenn Fehler bei der Konfiguration auftreten.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }


    /**
     * Bean für den UserDetailsService.
     *
     * @param bCryptPasswordEncoder Das BCryptPasswordEncoder-Objekt.
     * @return das UserDetailsService-Objekt.
     */
    @Bean
    public UserDetailsService userDetailsService(EmployeeService employeeService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return username -> {
            Employee employee = employeeService.getEmployeeByNick(username);
            if (employee == null) {
                throw new UsernameNotFoundException("Mitarbeiter mit dem nick " + username + " nicht gefunden!");
            }
            return User.builder()
                    .username(employee.getNick())
                    .password(bCryptPasswordEncoder.encode(employee.getPassword()))
                    .roles(employee.getRole().toString())
                    .build();
        };
    }

}
