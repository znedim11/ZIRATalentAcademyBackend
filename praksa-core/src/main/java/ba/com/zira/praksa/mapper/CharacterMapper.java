package ba.com.zira.praksa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ba.com.zira.praksa.api.model.character.CharacterResponse;
import ba.com.zira.praksa.api.model.character.CompleteCharacterResponse;
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

    @Mapping(source = "id", target = "characterId")
    @Mapping(source = "name", target = "fullName")
    @Mapping(source = "realName", target = "realName")
    CompleteCharacterResponse characterEntityToCompleteCharacter(CharacterEntity characterEntity);
}
