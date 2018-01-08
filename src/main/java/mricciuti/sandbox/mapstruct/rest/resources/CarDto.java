package mricciuti.sandbox.mapstruct.rest.resources;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarDto extends BaseDto {
    
    private int seatCount;
    
    private String maker;

}
