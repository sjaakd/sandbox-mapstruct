package mricciuti.sandbox.mapstruct.rest.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import mricciuti.sandbox.mapstruct.entities.Car;
import mricciuti.sandbox.mapstruct.rest.resources.CarDto;

@Mapper(config = EntityToDtoMappingConfig.class)
public abstract class CarMapper {

    @Mapping(source = "manufacturer", target = "maker")
    @Mapping(source = "numberOfSeats", target = "seatCount")
    // additional mapping inherited from  EntityToDtoMappingConfig.entityToDto method :
    //  @Mapping(target = "dbId", source = "id")
    //  @Mapping(target = "links", ignore = true)
    abstract public CarDto mapTo(Car car);
    
    @InheritInverseConfiguration(name = "mapTo")
    // inheritInvers configuration should map both maker and seatCount properties.
    // additional mapping should also be inherited from   EntityToDtoMappingConfig.dtoToEntity method,
    // BUT THIS MAPPINGS ARE NOT APPLIED  (see junit test)
    //    @Mapping(target = "id", source = "dbId")
    //    @Mapping(target = "createdBy", ignore = true)
    //    @Mapping(target = "creationDate", ignore = true)
    //    @Mapping(target = "lastModifiedBy", constant = "restApiUser") // force modifiedBy with restApiUser constant
    //    @Mapping(target = "lastModifiedDate", ignore = true)
    abstract Car mapFrom(CarDto carDto);
    
}
