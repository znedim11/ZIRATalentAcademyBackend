package ba.com.zira.praksa.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.dao.model.ObjectEntity;

/**
 * Defined mapper interface for mapping a DTO to Entity model class
 *
 * @author zira
 *
 */

@Mapper(componentModel = "spring")
public interface ObjectMapper {

    List<ObjectResponse> entityListToDtoList(List<ObjectEntity> entityList);
}
