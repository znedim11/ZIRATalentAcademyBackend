package ba.com.zira.praksa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ba.com.zira.praksa.api.model.linkMap.LinkRequest;
import ba.com.zira.praksa.dao.model.LinkMapEntity;

/**
 * Defined mapper interface for mapping a DTO to Entity model class
 *
 * @author zira
 *
 */
@Mapper(componentModel = "spring")
public interface LinkMapMapper {
    LinkMapMapper INSTANCE = Mappers.getMapper(LinkMapMapper.class);

    LinkMapEntity linkRequestToEntity(LinkRequest entity);
}