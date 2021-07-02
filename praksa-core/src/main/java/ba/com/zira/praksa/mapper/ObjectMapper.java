package ba.com.zira.praksa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import ba.com.zira.commons.model.PagedData;
import ba.com.zira.praksa.api.model.object.ObjectCreateRequest;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.api.model.object.ObjectUpdateRequest;
import ba.com.zira.praksa.dao.model.ObjectEntity;

/**
 * Defined mapper interface for mapping a DTO to Entity model class <br>
 * for Object
 *
 * @author zira
 *
 */
@Mapper(componentModel = "spring")
public interface ObjectMapper {

    ObjectMapper INSTANCE = Mappers.getMapper(ObjectMapper.class);

    ObjectEntity dtoToEntity(ObjectResponse sample);

    ObjectEntity dtoToEntity(ObjectCreateRequest sample);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateDtoToEntity(ObjectUpdateRequest objectDto, @MappingTarget ObjectEntity objectEntity);

    ObjectResponse entityToDto(ObjectEntity objectEntity);

    PagedData<ObjectResponse> entitiesToDtos(PagedData<ObjectEntity> objectEntities);

    ObjectCreateRequest updateToCreateRequest(ObjectUpdateRequest update);

    List<ObjectResponse> entityListToDtoList(List<ObjectEntity> entityList);

}