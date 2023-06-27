package com.itkolleg.bookingsystem.config;

import com.itkolleg.bookingsystem.domains.TimeSlot;
import com.itkolleg.bookingsystem.service.Desk.DeskService;
import com.itkolleg.bookingsystem.service.DeskBooking.DeskBookingService;
import com.itkolleg.bookingsystem.service.Employee.EmployeeService;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.service.Holiday.HolidayService;
import com.itkolleg.bookingsystem.service.TimeSlot.TimeSlotService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Set;

/**
 * Konfigurationsklasse für die Sicherheitseinstellungen.
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final EmployeeService employeeService;
    private final DeskService deskService;
    private final DeskBookingService deskBookingService;
    private final HolidayService holidayService;
    private final TimeSlotService timeSlotService;


   // public WebSecurityConfig(EmployeeService employeeService) {
   //     this.employeeService = employeeService;

    public WebSecurityConfig(EmployeeService employeeService, DeskService deskService, DeskBookingService deskBookingService, TimeSlotService timeSlotService, HolidayService holidayService) {
        this.employeeService = employeeService;
        this.deskService = deskService;
        this.deskBookingService = deskBookingService;
        this.holidayService = holidayService;
        this.timeSlotService= timeSlotService;

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
                /* .headers()
                 .contentTypeOptions()
                 .disable()
                 .and() */
                .authorizeHttpRequests(authConfig -> {
                    authConfig.requestMatchers(HttpMethod.GET, "/web/**", "/web/login", "/error", "/web/login-error", "/web/logout", "/static/**", "/templates/**").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/web/login", "web/**").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET, "/web/**").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.GET,  "/web/**").hasAnyRole("OPERATOR", "N_EMPLOYEE", "P_EMPLOYEE");
                    authConfig.requestMatchers(HttpMethod.POST, "/web/**").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.POST, "/web/**").hasAnyRole("OPERATOR", "N_EMPLOYEE", "P_EMPLOYEE");
                    /*authConfig.requestMatchers(HttpMethod.GET, "/web/hello", "/web/**","/web/login", "/error", "/web/login-error", "/web/logout", "/static/**", "/templates/**").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/web/**", "/web/login").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET, "/web/allemployees", "/web/insertemployeeform", "/web/insertemployee", "/web/admin-start", "/web/editemployee/**", "/web/deleteemployee/**").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.GET, "/web/deskbookings/admin/**","/web/desks/**","/web/admin-start" ).hasAnyRole("ADMIN", "OPERATOR");
                    authConfig.requestMatchers(HttpMethod.POST, "/web/deskbookings/admin/**","/web/desks/**", "/web/admin-start" ).hasAnyRole("ADMIN", "OPERATOR");
                    */
                    /*authConfig.requestMatchers(HttpMethod.GET, "/web").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.GET, "/operator").hasRole("OPERATOR");
                    authConfig.requestMatchers(HttpMethod.GET, "/users").hasAnyRole("Admin", "DEVELOPER");
                    authConfig.requestMatchers(HttpMethod.GET, "/authorities").hasAnyRole("DEVELOPER");
                    authConfig.anyRequest().authenticated();*/
                })
                .formLogin(login -> {
                    login.loginPage("/web/login")
                            .successHandler((request, response, authentication) -> {
                                Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
                                if (roles.contains("ROLE_ADMIN")) {
                                    response.sendRedirect("/web/admin-start");
                                } else if (roles.contains("ROLE_OPERATOR")) {
                                    response.sendRedirect("/web/hello");
                                } else if (roles.contains("ROLE_N_EMPLOYEE")) {
                                    response.sendRedirect("/web/hello");
                                } else if (roles.contains("ROLE_P_EMPLOYEE")) {
                                    response.sendRedirect("/web/hello");
                                } else {
                                    response.sendRedirect("/web/login-error");
                                }
                            })
                            .failureUrl("/web/login-error");
                })

                .logout(logout -> {
                    logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
                    logout.logoutSuccessUrl("/web/logout");
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
    public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return username -> {
            Employee employee = employeeService.getEmployeeByNick(username);
            if (employee == null) {
                throw new UsernameNotFoundException("Mitarbeiter mit dem nick " + username + " nicht gefunden!");
            }
            return User.builder()
                    .username(employee.getNick())
                    .password(bCryptPasswordEncoder.encode(employee.getPassword()))
                    .authorities(employee.getAuthorities())
                    .build();
        };
    }





}
