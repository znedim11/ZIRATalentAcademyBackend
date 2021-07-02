package ba.com.zira.praksa.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ba.com.zira.praksa.api.model.media.Media;
import ba.com.zira.praksa.dao.model.MediaEntity;

@Mapper(componentModel = "spring")
public interface MediaMapper {

    @InheritInverseConfiguration(name = "dtoToEntity")
    Media entityToDto(MediaEntity mediaEntity);

    GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

    MediaEntity dtoToEntity(Media entity);

    /*
     * @Mapping(source = "fullName", target = "fullName") Game
     * gameEntityToGame(GameEntity sampleModelEntity);
     *
     * @Mapping(source = "fullName", target = "fullName") GameEntity
     * gameToGameEntity(Game sampleModel);
     *
     * GameEntity dtoToEntity(Game sample);
     *
     * @InheritInverseConfiguration(name = "dtoToEntity") Game
     * entityToDto(GameEntity billingPeriodEntity);
     */

}
