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
import ba.com.zira.praksa.api.PersonService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.character.CharacterResponse;
import ba.com.zira.praksa.api.model.concept.ConceptResponse;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.api.model.game.GameResponse;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.api.model.person.Person;
import ba.com.zira.praksa.api.model.person.PersonCreateRequest;
import ba.com.zira.praksa.api.model.person.PersonUpdateRequest;
import ba.com.zira.praksa.core.validation.PersonRequestValidation;
import ba.com.zira.praksa.dao.PersonDAO;
import ba.com.zira.praksa.dao.model.CharacterEntity;
import ba.com.zira.praksa.dao.model.CharacterEntity_;
import ba.com.zira.praksa.dao.model.ConceptEntity;
import ba.com.zira.praksa.dao.model.ConceptEntity_;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.dao.model.GameEntity_;
import ba.com.zira.praksa.dao.model.LinkMapEntity_;
import ba.com.zira.praksa.dao.model.LocationEntity_;
import ba.com.zira.praksa.dao.model.ObjectEntity;
import ba.com.zira.praksa.dao.model.ObjectEntity_;
import ba.com.zira.praksa.dao.model.PersonEntity;
import ba.com.zira.praksa.mapper.CharacterMapper;
import ba.com.zira.praksa.mapper.ConceptMapper;
import ba.com.zira.praksa.mapper.GameMapper;
import ba.com.zira.praksa.mapper.ObjectMapper;
import ba.com.zira.praksa.mapper.PersonMapper;

@Service
public class PersonServiceImpl implements PersonService {

    RequestValidator requestValidator;
    PersonRequestValidation personRequestValidation;
    PersonDAO personDAO;
    PersonMapper personMapper;
    GameMapper gameMapper;
    ConceptMapper conceptMapper;
    CharacterMapper characterMapper;
    ObjectMapper objectMapper;

    static final String VALIDATION_RULE = "validateAbstractRequest";

    public PersonServiceImpl(RequestValidator requestValidator, PersonRequestValidation personRequestValidation, PersonDAO personDAO,
            PersonMapper personMapper, GameMapper gameMapper, ConceptMapper conceptMapper, CharacterMapper characterMapper,
            ObjectMapper objectMapper) {
        super();
        this.requestValidator = requestValidator;
        this.personRequestValidation = personRequestValidation;
        this.personDAO = personDAO;
        this.personMapper = personMapper;
        this.gameMapper = gameMapper;
        this.conceptMapper = conceptMapper;
        this.characterMapper = characterMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public PagedPayloadResponse<Person> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<PersonEntity> personModelEntities = personDAO.findAll(request.getFilter());
        final List<PersonEntity> personEntityList = personModelEntities.getRecords();

        final List<Person> personList = personMapper.entityListToDtoList(personEntityList);

        PagedData<Person> data = new PagedData<>();
        data.setNumberOfPages(personModelEntities.getNumberOfPages());
        data.setRecords(personList);
        data.setNumberOfRecords(personModelEntities.getNumberOfRecords());
        data.setPage(personModelEntities.getPage());
        data.setRecordsPerPage(personModelEntities.getRecordsPerPage());
        data.setHasMoreRecords(personModelEntities.hasMoreRecords());

        return new PagedPayloadResponse<>(request, ResponseCode.OK, data);
    }

    @Override
    public PayloadResponse<Person> findById(final SearchRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        personRequestValidation.validatePersonRequest(entityRequest, VALIDATION_RULE);

        final PersonEntity personEntity = personDAO.findByPK(request.getEntity());

        return new PayloadResponse<>(request, ResponseCode.OK, personMapper.entityToDto(personEntity));
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<Person> create(EntityRequest<PersonCreateRequest> request) throws ApiException {
        personRequestValidation.validatePersonLastName(request, VALIDATION_RULE);

        PersonEntity personEntity = personMapper.personCreateToPersonEntity(request.getEntity());
        personEntity.setCreated(LocalDateTime.now());
        personEntity.setCreatedBy(request.getUserId());

        personDAO.persist(personEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, personMapper.entityToDto(personEntity));
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<Person> update(final EntityRequest<PersonUpdateRequest> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity().getId(), request);
        personRequestValidation.validatePersonRequest(entityRequest, VALIDATION_RULE);

        final PersonUpdateRequest requestEntity = request.getEntity();
        final PersonEntity personEntity = personMapper.personUpdateToPersonEntity(requestEntity);
        personEntity.setModified(LocalDateTime.now());
        personEntity.setModifiedBy(request.getUserId());

        personDAO.merge(personEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, personMapper.entityToDto(personEntity));
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        personRequestValidation.validatePersonRequest(entityRequest, VALIDATION_RULE);

        personDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Person deleted!");
    }

    @Override
    public ListPayloadResponse<GameResponse> getGamesForPerson(EntityRequest<Long> request) throws ApiException {
        List<GameEntity> listOfGameEntities = personDAO.getGamesForPerson(request.getEntity());

        return new ListPayloadResponse<>(request, ResponseCode.OK, gameMapper.gameEntitesToGames(listOfGameEntities));
    }

    @Override
    public ListPayloadResponse<ConceptResponse> getConceptsForPerson(EntityRequest<Long> request) throws ApiException {
        List<ConceptEntity> listOfConceptEntities = personDAO.getConceptsForPerson(request.getEntity());

        return new ListPayloadResponse<>(request, ResponseCode.OK, conceptMapper.entityListToResponseList(listOfConceptEntities));
    }

    @Override
    public ListPayloadResponse<ObjectResponse> getObjectsForPerson(EntityRequest<Long> request) throws ApiException {
        List<ObjectEntity> listOfObjectEntities = personDAO.getObjectsForPerson(request.getEntity());
        return new ListPayloadResponse<>(request, ResponseCode.OK, objectMapper.entityListToDtoList(listOfObjectEntities));
    }

    @Override
    public ListPayloadResponse<CharacterResponse> getCharactersForPerson(EntityRequest<Long> request) throws ApiException {
        List<CharacterEntity> listOfCharacterEntities = personDAO.getCharactersForPerson(request.getEntity());
        return new ListPayloadResponse<>(request, ResponseCode.OK, characterMapper.entityListToDtoList(listOfCharacterEntities));

    }

    @Override
    public ListPayloadResponse<LoV> getLoVs(final ListRequest<Long> request) throws ApiException {
        if (request.getList() != null) {
            for (Long item : request.getList()) {
                EntityRequest<Long> longRequest = new EntityRequest<>(item, request);
                personRequestValidation.validatePersonRequest(longRequest, VALIDATION_RULE);
            }
        }

        List<LoV> loVs = personDAO.getLoVs(request.getList());

        return new ListPayloadResponse<>(request, ResponseCode.OK, loVs);
    }

    @Override
    public ListPayloadResponse<LoV> getLoVsNotConnectedTo(final EntityRequest<LoV> request) throws ApiException {
        requestValidator.validate(request);

        String field = null;
        String idField = null;
        Long id = request.getEntity().getId();

        if (ObjectType.CHARACTER.getValue().equalsIgnoreCase(request.getEntity().getName())) {
            field = LinkMapEntity_.character.getName();
            idField = CharacterEntity_.id.getName();
        } else if (ObjectType.CONCEPT.getValue().equalsIgnoreCase(request.getEntity().getName())) {
            field = LinkMapEntity_.concept.getName();
            idField = ConceptEntity_.id.getName();
        } else if (ObjectType.GAME.getValue().equalsIgnoreCase(request.getEntity().getName())) {
            field = LinkMapEntity_.game.getName();
            idField = GameEntity_.id.getName();
        } else if (ObjectType.LOCATION.getValue().equalsIgnoreCase(request.getEntity().getName())) {
            field = LinkMapEntity_.location.getName();
            idField = LocationEntity_.id.getName();
        } else if (ObjectType.OBJECT.getValue().equalsIgnoreCase(request.getEntity().getName())) {
            field = LinkMapEntity_.object.getName();
            idField = ObjectEntity_.id.getName();
        }

        List<LoV> loVs = null;
        if (field != null) {
            loVs = personDAO.getLoVsNotConnectedTo(field, idField, id);
        }

        return new ListPayloadResponse<>(request, ResponseCode.OK, loVs);
    }
}
