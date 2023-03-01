package com.itkolleg.bookingsystem.domains;

import com.google.auto.value.extension.serializable.SerializableAutoValue;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class Point implements Serializable {
    private String x;
    private String y;
}
