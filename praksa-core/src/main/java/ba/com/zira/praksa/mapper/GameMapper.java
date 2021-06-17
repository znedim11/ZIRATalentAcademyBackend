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
public interface GameMapper
{

	GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

	@Mapping(source = "fullName", target = "fullName")
	GameResponse gameEntityToGame(GameEntity gameModelEntity);

	@Mapping(source = "fullName", target = "fullName")
	GameEntity gameToGameEntity(GameCreateRequest gameModel);

	@Mapping(target = "created", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	void updateForGameUpdate(GameUpdateRequest gameModel, @MappingTarget GameEntity gameEntity);

	GameEntity responseToEntity(GameResponse gameResponse);

	GameEntity dtoToEntity(GameCreateRequest gameRequest);

}