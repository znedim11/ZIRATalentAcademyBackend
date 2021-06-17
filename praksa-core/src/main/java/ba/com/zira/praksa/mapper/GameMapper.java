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

    GameEntity dtoToEntity(Game dto);

    Game entityToDto(GameEntity entity);

    List<Game> entityListToDtoList(List<GameEntity> entityList);

}