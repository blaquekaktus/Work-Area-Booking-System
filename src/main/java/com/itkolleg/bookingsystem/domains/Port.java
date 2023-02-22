package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Port {
    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Porttype porttype;

    @Size(min = 2)
    private String name;

    public Port(String name){
        this.name=name;
        this.porttype = porttype;
    }
}
