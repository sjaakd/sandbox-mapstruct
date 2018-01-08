package mricciuti.sandbox.mapstruct.entities;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Car extends BaseEntity {
    
    private static final long serialVersionUID = 6881178475052097485L;
    
    private String manufacturer;

    private int numberOfSeats;
    
}
