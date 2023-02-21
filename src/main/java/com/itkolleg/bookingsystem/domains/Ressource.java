package com.itkolleg.bookingsystem.domains;


import jakarta.persistence.*;
import lombok.*;


/**
 * This Class Represents the Domain Class for Ressources and helds all important Datafields and a Constructor
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Ressource extends Bookable{
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    private Ressourcetype ressourcetype;

    private String name;
    private String description;
    private String info;
    private String serialnumber;
    private int amount;

    public Ressource(Long id, Ressourcetype ressourcetype,String name, String description, String info, String serialnumber, int amount) {
        this.id = id;
        this.ressourcetype = ressourcetype;
        this.description = description;
        this.info = info;
        this.name= name;
        this.serialnumber = serialnumber;
        this.amount = amount;
    }


}

