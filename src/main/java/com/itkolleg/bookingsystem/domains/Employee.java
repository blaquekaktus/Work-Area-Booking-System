package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

@Entity
@Table(name="employee")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Employee implements UserDetails {

    @Id
    //@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //limit length of name
    @Size(min = 2, max = 50)
    //@Column(name="FNAME")
    private String fname;

    @Size(min = 2, max = 50)
    //@Column(name="LNAME")
    private String lname;

    //nick darf leer sein
    @Size(max = 10)
    //@Column(name="NICK")
    private String nick;

    @Email
    //@Column(name="EMAIL")
    private String email;



    //@Column(name="PASSWORD")
    private String password;

    private Role role;

    public Employee(String fname, String lname, String nick, String email, String password, Role role){
        this.fname=fname;
        this.lname=lname;
        this.nick=nick;
        this.email=email;
        this.password=password;
        this.role=role;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
