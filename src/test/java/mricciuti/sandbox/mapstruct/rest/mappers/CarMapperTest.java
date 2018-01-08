package mricciuti.sandbox.mapstruct.rest.mappers;

import static org.hamcrest.core.Is.is;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import mricciuti.sandbox.mapstruct.entities.Car;
import mricciuti.sandbox.mapstruct.rest.resources.CarDto;

public class CarMapperTest {

    private CarMapper mapper = Mappers.getMapper(CarMapper.class);
    
    @Test
    public void testMapEntityToDto() {
        final CarDto dto = this.mapper.mapTo(newCar());
        Assert.assertThat(dto.getDbId(), is(9l));
        Assert.assertThat(dto.getMaker(), is("Nissan"));
        Assert.assertThat(dto.getSeatCount(), is(5));
    }
    
    @Test
    public void testMapDtoToEntity() {
        final Car car = this.mapper.mapFrom(newCarDto());
        Assert.assertThat(car.getId(), is(9l));
        Assert.assertThat(car.getManufacturer(), is("Nissan"));
        Assert.assertThat(car.getNumberOfSeats(), is(5));

        // following assertion fails :  createdBy property is not set.
        // It should have been set as defined by   EntityToDtoMappingConfig.dtoToEntity mapping method:
        //     @Mapping(target = "lastModifiedBy", constant = "restApiUser")
        Assert.assertThat("Property lastModifiedBy has not been mapped properly", car.getLastModifiedBy(), is("restApiUser"));
    }

    private Car newCar() {
        final Car car = new Car();
        car.setId(9l);
        car.setCreatedBy("admin");
        car.setCreationDate(new Date());
        car.setManufacturer("Nissan");
        car.setNumberOfSeats(5);
        return car;
    }
    
    private CarDto newCarDto() {
        final CarDto carDto = new CarDto();
        carDto.setDbId(9l);
        carDto.setMaker("Nissan");
        carDto.setSeatCount(5);
        return carDto;
    }
    
}
