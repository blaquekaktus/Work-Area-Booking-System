package com.itkolleg.bookingsystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Konfigurationsklasse für die Sicherheitseinstellungen.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Konfiguriert die HttpSecurity-Einstellungen für die Anforderungsautorisierung.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN") // Zugriff nur für Benutzer mit der Rolle "ADMIN" auf "/admin/**"
                .antMatchers("/user/**").hasAnyRole("ADMIN", "USER") // Zugriff für Benutzer mit der Rolle "ADMIN" oder "USER" auf "/user/**"
                .anyRequest().authenticated() // Authentifizierung für alle anderen Anfragen erforderlich
                .and()
                .formLogin() // Verwenden Sie das Standard-Login-Formular von Spring Security
                .and()
                .logout(); // Verarbeitung der Standardabmeldung von Spring Security
    }

    /**
     * Konfiguriert den globalen Authentifizierungsmechanismus.
     * Hier werden Benutzerkonten und Rollen im Arbeitsspeicher konfiguriert.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication() // Konfiguriert die Authentifizierung im Arbeitsspeicher
                .withUser("admin").password("{noop}admin123").roles("ADMIN") // Konfiguriert einen Benutzer "admin" mit Passwort "admin123" und Rolle "ADMIN"
                .and()
                .withUser("user").password("{noop}user123").roles("USER"); // Konfiguriert einen Benutzer "user" mit Passwort "user123" und Rolle "USER"
    }
}
