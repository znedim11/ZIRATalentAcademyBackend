package ba.com.zira.praksa.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import ba.com.zira.praksa.api.model.media.Media;
import ba.com.zira.praksa.dao.model.MediaEntity;

@Mapper(componentModel = "spring")
public interface MediaMapper {

    @InheritInverseConfiguration(name = "dtoToEntity")
    Media entityToDto(MediaEntity mediaEntity);

    MediaEntity dtoToEntity(Media entity);

}
