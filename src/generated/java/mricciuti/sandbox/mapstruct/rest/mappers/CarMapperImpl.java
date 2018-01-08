package mricciuti.sandbox.mapstruct.rest.mappers;

import javax.annotation.Generated;
import mricciuti.sandbox.mapstruct.entities.Car;
import mricciuti.sandbox.mapstruct.rest.resources.CarDto;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2018-01-08T14:52:49+0100",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class CarMapperImpl extends CarMapper {

    @Override
    public CarDto mapTo(Car car) {
        if ( car == null ) {
            return null;
        }

        CarDto carDto = new CarDto();

        carDto.setDbId( car.getId() );
        carDto.setMaker( car.getManufacturer() );
        carDto.setSeatCount( car.getNumberOfSeats() );

        return carDto;
    }

    @Override
    Car mapFrom(CarDto carDto) {
        if ( carDto == null ) {
            return null;
        }

        Car car = new Car();

        car.setNumberOfSeats( carDto.getSeatCount() );
        car.setId( carDto.getDbId() );
        car.setManufacturer( carDto.getMaker() );

        return car;
    }
}
