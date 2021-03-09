package ba.com.zira.praksa.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import ba.com.zira.praksa.api.model.game.Game;
import ba.com.zira.praksa.dao.model.GameEntity;

/**
 * Defined mapper interface for mapping a DTO to Entity model class
 *
 * @author zira
 *
 */
@Mapper(componentModel = "spring")
public interface GameMapper {

    GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

    @Mapping(source = "fullName", target = "fullName")
    Game gameEntityToGame(GameEntity sampleModelEntity);

    @Mapping(source = "fullName", target = "fullName")
    GameEntity gameToGameEntity(Game sampleModel);

    GameEntity dtoToEntity(Game sample);

    @InheritInverseConfiguration(name = "dtoToEntity")
    Game entityToDto(GameEntity billingPeriodEntity);

}