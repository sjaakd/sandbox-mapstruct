package mricciuti.sandbox.mapstruct.rest.mappers;

import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.ReportingPolicy;

import mricciuti.sandbox.mapstruct.entities.BaseEntity;
import mricciuti.sandbox.mapstruct.rest.resources.BaseDto;

/**
 * Shared configuration from Entities to Dto instances
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
