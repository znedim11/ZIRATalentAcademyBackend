package ba.com.zira.praksa.mapper;

import java.util.List;

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

    Location entityToDto(LocationEntity billingPeriodEntity);

    List<Location> entityListToDtoList(List<LocationEntity> entityList);

}