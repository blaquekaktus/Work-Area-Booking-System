package com.itkolleg.bookingsystem.domains;


import jakarta.persistence.*;
import lombok.*;



@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Ressource {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    private Ressourcetype ressourcetype;

    private String name;

    private String info;

    public Ressource(Long id, Ressourcetype ressourcetype,String name, String info) {
        this.id = id;
        this.ressourcetype = ressourcetype;
        this.info = info;
        this.name= name;
    }


}

