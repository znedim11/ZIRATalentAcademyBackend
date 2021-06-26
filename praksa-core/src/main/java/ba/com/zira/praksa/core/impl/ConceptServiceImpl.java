/**
 *
 */
package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.ConceptService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.character.CharacterResponse;
import ba.com.zira.praksa.api.model.concept.ConceptCreateRequest;
import ba.com.zira.praksa.api.model.concept.ConceptResponse;
import ba.com.zira.praksa.api.model.concept.ConceptSearchRequest;
import ba.com.zira.praksa.api.model.concept.ConceptUpdateRequest;
import ba.com.zira.praksa.api.model.game.GameResponse;
import ba.com.zira.praksa.api.model.location.Location;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.api.model.person.Person;
import ba.com.zira.praksa.core.validation.ConceptRequestValidation;
import ba.com.zira.praksa.dao.ConceptDAO;
import ba.com.zira.praksa.dao.model.CharacterEntity;
import ba.com.zira.praksa.dao.model.ConceptEntity;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.dao.model.LocationEntity;
import ba.com.zira.praksa.dao.model.ObjectEntity;
import ba.com.zira.praksa.dao.model.PersonEntity;
import ba.com.zira.praksa.mapper.CharacterMapper;
import ba.com.zira.praksa.mapper.ConceptMapper;
import ba.com.zira.praksa.mapper.GameMapper;
import ba.com.zira.praksa.mapper.LocationMapper;
import ba.com.zira.praksa.mapper.ObjectMapper;
import ba.com.zira.praksa.mapper.PersonMapper;

/**
 * @author zira
 *
 */

@Service
public class ConceptServiceImpl implements ConceptService {

    static final String VALIDATE_ABSTRACT_REQUEST = "validateAbstractRequest";
    static final String BASIC_NOT_NULL = "basicNotNull";

    RequestValidator requestValidator;
    ConceptRequestValidation conceptRequestValidation;
    ConceptDAO conceptDAO;
    ConceptMapper conceptMapper;
    GameMapper gameMapper;
    PersonMapper personMapper;
    ObjectMapper objectMapper;
    CharacterMapper characterMapper;
    LocationMapper locationMapper;

    public ConceptServiceImpl(RequestValidator requestValidator, ConceptRequestValidation conceptRequestValidation, ConceptDAO conceptDAO,
            ConceptMapper conceptMapper, GameMapper gameMapper, PersonMapper personMapper, ObjectMapper objectMapper,
            CharacterMapper characterMapper, LocationMapper locationMapper) {
        super();
        this.requestValidator = requestValidator;
        this.conceptRequestValidation = conceptRequestValidation;
        this.conceptDAO = conceptDAO;
        this.conceptMapper = conceptMapper;
        this.gameMapper = gameMapper;
        this.personMapper = personMapper;
        this.objectMapper = objectMapper;
        this.characterMapper = characterMapper;
        this.locationMapper = locationMapper;
    }

    @Override
    public PagedPayloadResponse<ConceptResponse> find(SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<ConceptEntity> conceptEntitesData = conceptDAO.findAll(request.getFilter());
        List<ConceptEntity> conceptEntities = conceptEntitesData.getRecords();

        final List<ConceptResponse> conceptList = conceptMapper.entityListToResponseList(conceptEntities);

        PagedData<ConceptResponse> pagedData = new PagedData<>();
        pagedData.setNumberOfPages(conceptEntitesData.getNumberOfPages());
        pagedData.setNumberOfRecords(conceptEntitesData.getNumberOfRecords());
        pagedData.setPage(conceptEntitesData.getPage());
        pagedData.setRecords(conceptList);
        pagedData.setRecordsPerPage(conceptEntitesData.getRecordsPerPage());

        return new PagedPayloadResponse<>(request, ResponseCode.OK, pagedData);
    }

    @Override
    public PayloadResponse<ConceptResponse> findById(SearchRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        conceptRequestValidation.validateConceptExists(entityRequest, VALIDATE_ABSTRACT_REQUEST);

        final ConceptEntity conceptEntity = conceptDAO.findByPK(request.getEntity());

        final ConceptResponse conceptResponse = conceptMapper.entityToResponse(conceptEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, conceptResponse);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<ConceptResponse> create(EntityRequest<ConceptCreateRequest> request) throws ApiException {
        conceptRequestValidation.validateEntityExistsInCreateRequest(request, BASIC_NOT_NULL);

        EntityRequest<String> entityRequest = new EntityRequest<>(request.getEntity().getName(), request);
        conceptRequestValidation.validateConceptNameExists(entityRequest, BASIC_NOT_NULL);

        ConceptEntity entity = conceptMapper.createRequestToEntity(request.getEntity());
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(request.getUserId());

        conceptDAO.persist(entity);

        ConceptResponse response = conceptMapper.entityToResponse(entity);

        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<ConceptResponse> update(final EntityRequest<ConceptUpdateRequest> request) throws ApiException {
        conceptRequestValidation.validateEntityExistsInUpdateRequest(request, BASIC_NOT_NULL);

        EntityRequest<Long> entityRequestId = new EntityRequest<>(request.getEntity().getId(), request);
        conceptRequestValidation.validateConceptExists(entityRequestId, VALIDATE_ABSTRACT_REQUEST);

        EntityRequest<String> entityRequestName = new EntityRequest<>(request.getEntity().getName(), request);
        conceptRequestValidation.validateConceptNameExists(entityRequestName, BASIC_NOT_NULL);

        final ConceptUpdateRequest conceptRequest = request.getEntity();

        ConceptEntity conceptEntity = conceptDAO.findByPK(request.getEntity().getId());
        conceptEntity = conceptMapper.updateRequestToEntity(conceptRequest, conceptEntity);
        conceptEntity.setModified(LocalDateTime.now());
        conceptEntity.setModifiedBy(request.getUserId());

        conceptDAO.merge(conceptEntity);

        final ConceptResponse conceptResponse = conceptMapper.entityToResponse(conceptEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, conceptResponse);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        conceptRequestValidation.validateConceptExists(entityRequest, VALIDATE_ABSTRACT_REQUEST);

        conceptDAO.removeByPK(request.getEntity());

        return new PayloadResponse<>(request, ResponseCode.OK, "Concept deleted!");
    }

    @Override
    public ListPayloadResponse<GameResponse> getGamesByConcept(final EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        conceptRequestValidation.validateConceptExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        List<GameEntity> entityList = conceptDAO.getGamesByConcept(request.getEntity());
        List<GameResponse> gameList = gameMapper.gameEntitesToGames(entityList);

        return new ListPayloadResponse<>(request, ResponseCode.OK, gameList);
    }

    @Override
    public ListPayloadResponse<Person> getPersonsByConcept(final EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        conceptRequestValidation.validateConceptExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        List<PersonEntity> entityList = conceptDAO.getPersonsByConcept(request.getEntity());
        List<Person> personList = personMapper.entityListToDtoList(entityList);

        return new ListPayloadResponse<>(request, ResponseCode.OK, personList);
    }

    @Override
    public ListPayloadResponse<LoV> getLoVs(final ListRequest<Long> request) throws ApiException {
        if (request.getList() != null) {
            for (Long item : request.getList()) {
                EntityRequest<Long> longRequest = new EntityRequest<>(item, request);
                conceptRequestValidation.validateConceptExists(longRequest, VALIDATE_ABSTRACT_REQUEST);
            }
        }

        List<LoV> loVs = conceptDAO.getLoVs(request.getList());

        return new ListPayloadResponse<>(request, ResponseCode.OK, loVs);
    }

    @Override
    public ListPayloadResponse<ObjectResponse> getObjectsByConcept(EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        conceptRequestValidation.validateConceptExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        List<ObjectEntity> entityList = conceptDAO.getObjectsByConcept(request.getEntity());
        List<ObjectResponse> objectList = objectMapper.entityListToDtoList(entityList);

        return new ListPayloadResponse<>(request, ResponseCode.OK, objectList);
    }

    @Override
    public ListPayloadResponse<CharacterResponse> getCharactersByConcept(EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        conceptRequestValidation.validateConceptExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        List<CharacterEntity> entityList = conceptDAO.getCharactersByConcept(request.getEntity());
        List<CharacterResponse> characterList = characterMapper.entityListToDtoList(entityList);

        return new ListPayloadResponse<>(request, ResponseCode.OK, characterList);
    }

    @Override
    public ListPayloadResponse<Location> getLocationsByConcept(EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        conceptRequestValidation.validateConceptExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        List<LocationEntity> entityList = conceptDAO.getLocationsByConcept(request.getEntity());
        List<Location> locationList = locationMapper.entityListToDtoList(entityList);

        return new ListPayloadResponse<>(request, ResponseCode.OK, locationList);
    }

    @Override
    public PayloadResponse<Long> getNumberOfGamesByConcept(EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        conceptRequestValidation.validateConceptExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        Long numberofGames = conceptDAO.getNumberOfGamesByConcept(request.getEntity());

        return new PayloadResponse<>(request, ResponseCode.OK, numberofGames);
    }

    @Override
    public ListPayloadResponse<ConceptResponse> searchConcepts(EntityRequest<ConceptSearchRequest> request) throws ApiException {
        requestValidator.validate(request);

        List<ConceptEntity> conceptEntities = conceptDAO.searchConcepts(request.getEntity());
        List<ConceptResponse> conceptList = conceptMapper.entityListToResponseList(conceptEntities);

        return new ListPayloadResponse<>(request, ResponseCode.OK, conceptList);
    }

}
