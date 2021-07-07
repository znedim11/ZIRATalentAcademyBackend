package ba.com.zira.praksa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import ba.com.zira.praksa.api.model.concept.ConceptCreateRequest;
import ba.com.zira.praksa.api.model.concept.ConceptResponse;
import ba.com.zira.praksa.api.model.concept.ConceptUpdateRequest;
import ba.com.zira.praksa.dao.model.ConceptEntity;

/**
 * @author zira
 *
 */

@Mapper(componentModel = "spring")
public interface ConceptMapper {
    ConceptMapper INSTANCE = Mappers.getMapper(ConceptMapper.class);

    ConceptEntity responseToEntity(ConceptResponse conceptResponse);

    ConceptResponse entityToResponse(ConceptEntity conceptEntity);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    ConceptEntity updateRequestToEntity(ConceptUpdateRequest conceptUpdateRequest, @MappingTarget ConceptEntity conceptEntity);

    ConceptEntity createRequestToEntity(ConceptCreateRequest conceptCreateRequest);

    List<ConceptResponse> entityListToResponseList(List<ConceptEntity> conceptEntityList);
}