package ba.com.zira.praksa.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ba.com.zira.praksa.api.model.object.ObjectRequest;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.dao.model.ObjectEntity;

/**
 *
 * @author zira
 *
 */
@Mapper(componentModel = "spring")
public interface ObjectMapper {

    ObjectMapper INSTANCE = Mappers.getMapper(ObjectMapper.class);

    ObjectEntity dtoToEntity(ObjectRequest sample);

    @InheritInverseConfiguration(name = "dtoToEntity")
    ObjectResponse entityToDto(ObjectEntity sample);

    List<ObjectResponse> entityListToDtoList(List<ObjectEntity> entityList);

}