package ba.com.zira.praksa.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import ba.com.zira.praksa.api.model.platform.PlatformCreateRequest;
import ba.com.zira.praksa.api.model.platform.PlatformResponse;
import ba.com.zira.praksa.api.model.platform.PlatformUpdateRequest;
import ba.com.zira.praksa.dao.model.PlatformEntity;

@Mapper(componentModel = "spring")
public interface PlatformMapper {

    PlatformMapper INSTANCE = Mappers.getMapper(PlatformMapper.class);

    @Mapping(source = "fullName", target = "fullName")
    PlatformResponse platformEntityToPlatform(PlatformEntity platformEntity);

    @Mapping(source = "fullName", target = "fullName")
    PlatformEntity platformToPlatformEntity(PlatformCreateRequest request);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateForPlatformUpdate(PlatformUpdateRequest request, @MappingTarget PlatformEntity platformEntity);

    PlatformEntity dtoToEntity(PlatformCreateRequest request);

    @InheritInverseConfiguration(name = "dtoToEntity")
    PlatformResponse entityToDto(PlatformEntity platformEntity);
}
