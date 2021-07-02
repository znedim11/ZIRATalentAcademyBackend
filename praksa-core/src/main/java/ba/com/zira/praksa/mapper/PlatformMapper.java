package ba.com.zira.praksa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ba.com.zira.praksa.api.model.platform.PlatformCreateRequest;
import ba.com.zira.praksa.api.model.platform.PlatformResponse;
import ba.com.zira.praksa.api.model.platform.PlatformUpdateRequest;
import ba.com.zira.praksa.dao.model.PlatformEntity;

@Mapper(componentModel = "spring")
public interface PlatformMapper {

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateForPlatformUpdate(PlatformUpdateRequest request, @MappingTarget PlatformEntity platformEntity);

    PlatformEntity dtoToEntity(PlatformCreateRequest request);

    PlatformResponse entityToDto(PlatformEntity platformEntity);

    List<PlatformResponse> entityListToDtoList(List<PlatformEntity> entityList);
}
