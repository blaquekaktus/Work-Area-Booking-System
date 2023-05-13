package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.*;

@Embeddable
@NoArgsConstructor
@Getter
@Setter


public class Port {
    @Size(min=2)
    private String name;

    public Port(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}