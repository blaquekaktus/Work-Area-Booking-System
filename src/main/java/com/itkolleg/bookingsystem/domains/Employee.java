package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@NoArgsConstructor
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

    public Employee(String fname, String lname, String nick, String email, String password, Role role) {
        this.fname = fname;
        this.lname = lname;
        this.nick = nick;
        this.email = email;
        this.password = password;
        this.role = role;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.getNick();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
