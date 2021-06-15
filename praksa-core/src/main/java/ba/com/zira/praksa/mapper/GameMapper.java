package ba.com.zira.praksa.mapper;

import java.util.List;

import org.mapstruct.Mapper;

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

    // @Mapping(source = "fullName", target = "fullName")
    // Game gameEntityToGame(GameEntity sampleModelEntity);
    //
    // @Mapping(source = "fullName", target = "fullName")
    // GameEntity gameToGameEntity(Game sampleModel);

    GameEntity dtoToEntity(Game sample);

    // @InheritInverseConfiguration(name = "dtoToEntity")
    Game entityToDto(GameEntity billingPeriodEntity);

    List<Game> entityListToDtoList(List<GameEntity> entityList);

}