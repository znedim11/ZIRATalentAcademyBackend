package ba.com.zira.praksa.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import ba.com.zira.praksa.api.model.character.CharacterResponse;
import ba.com.zira.praksa.dao.model.CharacterEntity;

/**
 * Defined mapper interface for mapping a DTO to Entity model class
 *
 * @author zira
 *
 */

@Mapper(componentModel = "spring")
public interface CharacterMapper {

    List<CharacterResponse> entityListToDtoList(List<CharacterEntity> entityList);
}
