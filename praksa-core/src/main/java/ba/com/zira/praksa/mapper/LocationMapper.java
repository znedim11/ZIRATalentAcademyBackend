package ba.com.zira.praksa.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ba.com.zira.praksa.api.model.location.Location;
import ba.com.zira.praksa.dao.model.LocationEntity;

/**
 * Defined mapper interface for mapping a DTO to Entity model class
 *
 * @author zira
 *
 */
@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    LocationEntity dtoToEntity(Location sample);

    @InheritInverseConfiguration(name = "dtoToEntity")
    Location entityToDto(LocationEntity billingPeriodEntity);

    Location locationEntityToLocation(LocationEntity sampleModelEntity);

    LocationEntity locationToLocationEntity(Location sampleModel);

}