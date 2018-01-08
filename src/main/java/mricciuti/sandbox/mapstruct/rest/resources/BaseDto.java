package mricciuti.sandbox.mapstruct.rest.resources;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseDto {
    
    private Long dbId;

    private List<String> links;
}
