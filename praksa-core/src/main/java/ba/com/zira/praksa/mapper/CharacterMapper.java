package ba.com.zira.praksa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ba.com.zira.praksa.api.model.character.CharacterCreateRequest;
import ba.com.zira.praksa.api.model.character.CharacterResponse;
import ba.com.zira.praksa.api.model.character.CharacterUpdateRequest;
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

    CompleteCharacterResponse characterEntityToCompleteCharacter(CharacterEntity characterEntity);

    @Mapping(target = "dob", ignore = true)
    @Mapping(target = "dod", ignore = true)
    CharacterEntity dtoToEntity(CharacterCreateRequest characterDto);

    @Mapping(target = "dob", ignore = true)
    @Mapping(target = "dod", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateDtoToEntity(CharacterUpdateRequest characterDto, @MappingTarget CharacterEntity characterEntity);
}
