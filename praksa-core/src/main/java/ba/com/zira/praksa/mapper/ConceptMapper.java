package ba.com.zira.praksa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ba.com.zira.praksa.api.model.concept.Concept;
import ba.com.zira.praksa.api.model.concept.ConceptRequest;
import ba.com.zira.praksa.dao.model.ConceptEntity;

/**
 * @author irma
 *
 */

@Mapper(componentModel = "spring")
public interface ConceptMapper {
    ConceptMapper INSTANCE = Mappers.getMapper(ConceptMapper.class);

    ConceptEntity dtoToEntity(Concept concept);

    Concept entityToDto(ConceptEntity conceptEntity);

    ConceptEntity dtoRequestToEntity(ConceptRequest concept);
}
