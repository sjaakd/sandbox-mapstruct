# sandbox-mapstruct
Simple project to show issue with mapstruct and inheritance feature.

### Running project

After checkouting sources, execute:

```
./gradlew build
```

### Goal
In this project, Mapstruct is used to handle bi-directional mapping from/to  domain entity classes and REST dto classes.
The aim is to use the combination of following mapstruct features in order to factorize mapping definition as much as possible:
- shared configurations 
- inverse mapping  

_Shared configuration:_
Both Entity classes and DTO classes hierachies have a root base class used to handle common properties (e.g.:  ID field for entities)
The mapping rules for these common properties should be factorized in a single place, to avoid duplication in each Mapper implementations.
 
_Inverse mapping:_
For bi-directional mapping, the use of "@InheritInverseConfiguration" annotation can be used if mapping rules are "symetric".

### Implementation example
- Entities

```
/**
 * Common base class for entities classes
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    
    @Id
    private Long id;

    protected String createdBy;
    protected Date creationDate;
    protected String lastModifiedBy;    
    protected Date lastModifiedDate;
}

/**
 * Simple Car entity reprensentation
 */
@Entity
public class Car extends BaseEntity {

    private String manufacturer;
    private int numberOfSeats;   
}
```

- DTO classes
 
```
/**
 * Common base class for DTO classes
 */
public class BaseDto {
    
    private Long dbId;
    private List<String> links;
}

/**
 * Simple CAR DTO representation
 */
public class CarDto extends BaseDto {    
    private int seatCount;    
    private String maker;
}
```

- Mappers

```
/**
 * Shared configuration for mapping between Entities and Dto instances
 */
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.WARN,
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG)
public interface EntityToDtoMappingConfig {
    
    @Mapping(target = "dbId", source = "id")
    @Mapping(target = "links", ignore = true)
    BaseDto entityToDto(final BaseEntity entity);
    
    @Mapping(target = "id", source = "dbId")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastModifiedBy", constant = "restApiUser") // force modifiedBy with restApiUser constant
    @Mapping(target = "lastModifiedDate", ignore = true)
    public BaseEntity dtoToEntity(BaseDto dto);
    
}

/**
 * Specific Mapper class for Car entities
 */
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
```

### Issue

Mapping from Entities to DTO classes is working as expected: inheritance of mapping rules defined in the "EntityToDtoMappingConfig" config class is applied, in addition to rules defined in the specific CarMapper class.

But inheritance does not apply in the case of mapping from DTO to Entity (method mapFrom): this is possibly due to the use of @InheritInverseConfiguration annotation, which somehow would disable inheritance from mapping config class ( to be confirmed).

See unit test "CarMapperTest" to reproduce

```
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
```    

