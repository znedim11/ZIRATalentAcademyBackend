package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import ba.com.zira.praksa.api.GameService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.character.CharacterResponse;
import ba.com.zira.praksa.api.model.concept.ConceptResponse;
import ba.com.zira.praksa.api.model.feature.FeatureResponse;
import ba.com.zira.praksa.api.model.game.GameCreateRequest;
import ba.com.zira.praksa.api.model.game.GameResponse;
import ba.com.zira.praksa.api.model.game.GameUpdateRequest;
import ba.com.zira.praksa.api.model.gamefeature.GameFeatureCreateRequest;
import ba.com.zira.praksa.api.model.gamefeature.GameFeatureResponse;
import ba.com.zira.praksa.api.model.location.Location;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.api.model.person.Person;
import ba.com.zira.praksa.core.validation.FeatureRequestValidation;
import ba.com.zira.praksa.core.validation.GameRequestValidation;
import ba.com.zira.praksa.dao.FeatureDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.GameFeatureDAO;
import ba.com.zira.praksa.dao.model.CharacterEntity;
import ba.com.zira.praksa.dao.model.ConceptEntity;
import ba.com.zira.praksa.dao.model.FeatureEntity;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.dao.model.GameFeatureEntity;
import ba.com.zira.praksa.dao.model.LocationEntity;
import ba.com.zira.praksa.dao.model.ObjectEntity;
import ba.com.zira.praksa.dao.model.PersonEntity;
import ba.com.zira.praksa.mapper.CharacterMapper;
import ba.com.zira.praksa.mapper.ConceptMapper;
import ba.com.zira.praksa.mapper.FeatureMapper;
import ba.com.zira.praksa.mapper.GameFeatureMapper;
import ba.com.zira.praksa.mapper.GameMapper;
import ba.com.zira.praksa.mapper.LocationMapper;
import ba.com.zira.praksa.mapper.ObjectMapper;
import ba.com.zira.praksa.mapper.PersonMapper;

@Service
public class GameServiceImpl implements GameService {

    static final String VALIDATE_ABSTRACT_REQUEST = "validateAbstractRequest";

    RequestValidator requestValidator;
    GameRequestValidation gameRequestValidation;
    FeatureRequestValidation featureRequestValidation;

    GameDAO gameDAO;
    FeatureDAO featureDAO;
    GameFeatureDAO gameFeatureDAO;

    GameMapper gameMapper;
    ConceptMapper conceptMapper;
    PersonMapper personMapper;
    ObjectMapper objectMapper;
    CharacterMapper characterMapper;
    LocationMapper locationMapper;
    FeatureMapper featureMapper;
    GameFeatureMapper gameFeatureMapper;

    public GameServiceImpl(RequestValidator requestValidator, GameRequestValidation gameRequestValidation,
            FeatureRequestValidation featureRequestValidation, GameDAO gameDAO, FeatureDAO featureDAO, GameFeatureDAO gameFeatureDAO,
            GameMapper gameMapper, FeatureMapper featureMapper, GameFeatureMapper gameFeatureMapper, ConceptMapper conceptMapper,
            PersonMapper personMapper, ObjectMapper objectMapper, CharacterMapper characterMapper, LocationMapper locationMapper) {
        super();
        this.requestValidator = requestValidator;
        this.gameRequestValidation = gameRequestValidation;
        this.featureRequestValidation = featureRequestValidation;
        this.gameDAO = gameDAO;
        this.featureDAO = featureDAO;
        this.gameFeatureDAO = gameFeatureDAO;
        this.gameMapper = gameMapper;
        this.featureMapper = featureMapper;
        this.gameFeatureMapper = gameFeatureMapper;
        this.conceptMapper = conceptMapper;
        this.personMapper = personMapper;
        this.objectMapper = objectMapper;
        this.characterMapper = characterMapper;
        this.locationMapper = locationMapper;
    }

    @Override
    public PagedPayloadResponse<GameResponse> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<GameEntity> gameModelEntities = gameDAO.findAll(request.getFilter());
        final List<GameResponse> gameList = new ArrayList<>();

        for (final GameEntity GameEntity : gameModelEntities.getRecords()) {
            gameList.add(gameMapper.gameEntityToGame(GameEntity));
        }
        return new PagedPayloadResponse<>(request, ResponseCode.OK, gameList.size(), 1, 1, gameList.size(), gameList);
    }

    @Override
    public PayloadResponse<GameResponse> create(EntityRequest<GameCreateRequest> request) throws ApiException {
        requestValidator.validate(request);
        GameEntity entity = gameMapper.dtoToEntity(request.getEntity());
        gameDAO.persist(entity);
        GameResponse response = gameMapper.gameEntityToGame(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    public PayloadResponse<GameResponse> findById(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        final GameEntity gameEntity = gameDAO.findByPK(request.getEntity());

        final GameResponse game = gameMapper.gameEntityToGame(gameEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, game);
    }

    @Transactional(rollbackFor = ApiException.class)
    @Override
    public PayloadResponse<GameResponse> update(final EntityRequest<GameUpdateRequest> request) throws ApiException {
        requestValidator.validate(request);

        final GameUpdateRequest game = request.getEntity();
        GameEntity gameEntity = gameDAO.findByPK(game.getId());
        gameMapper.updateForGameUpdate(game, gameEntity);

        gameDAO.merge(gameEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, gameMapper.gameEntityToGame(gameEntity));

    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        gameDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Game deleted!");
    }

    @Override
    public ListPayloadResponse<ConceptResponse> getConceptsByGame(final EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        gameRequestValidation.validateIfGameExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        List<ConceptEntity> entityList = gameDAO.getConceptsByGame(request.getEntity());
        List<ConceptResponse> conceptList = conceptMapper.entityListToResponseList(entityList);

        return new ListPayloadResponse<>(request, ResponseCode.OK, conceptList);
    }

    @Override
    public ListPayloadResponse<Person> getPersonsByGame(final EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        gameRequestValidation.validateIfGameExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        List<PersonEntity> entityList = gameDAO.getPersonsByGame(request.getEntity());
        List<Person> personList = personMapper.entityListToDtoList(entityList);

        return new ListPayloadResponse<>(request, ResponseCode.OK, personList);
    }

    @Override
    public ListPayloadResponse<LoV> getLoVs(final ListRequest<Long> request) throws ApiException {
        for (Long item : request.getList()) {
            EntityRequest<Long> longRequest = new EntityRequest<>(item, request);
            gameRequestValidation.validateIfGameExists(longRequest, VALIDATE_ABSTRACT_REQUEST);
        }

        List<LoV> loVs = gameDAO.getLoVs(request.getList());

        return new ListPayloadResponse<>(request, ResponseCode.OK, loVs);
    }

    @Override
    public ListPayloadResponse<ObjectResponse> getObjectsByGame(EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        gameRequestValidation.validateIfGameExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        List<ObjectEntity> entityList = gameDAO.getObjectsByGame(request.getEntity());
        List<ObjectResponse> objectList = objectMapper.entityListToDtoList(entityList);

        return new ListPayloadResponse<>(request, ResponseCode.OK, objectList);
    }

    @Override
    public ListPayloadResponse<CharacterResponse> getCharactersByGame(EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        gameRequestValidation.validateIfGameExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        List<CharacterEntity> entityList = gameDAO.getCharactersByGame(request.getEntity());
        List<CharacterResponse> characterList = characterMapper.entityListToDtoList(entityList);

        return new ListPayloadResponse<>(request, ResponseCode.OK, characterList);
    }

    @Override
    public ListPayloadResponse<Location> getLocationsByGame(EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        gameRequestValidation.validateIfGameExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        List<LocationEntity> entityList = gameDAO.getLocationsByGame(request.getEntity());
        List<Location> locationList = locationMapper.entityListToDtoList(entityList);

        return new ListPayloadResponse<>(request, ResponseCode.OK, locationList);
    }

    @Override
    public PayloadResponse<Long> getNumberOfReleasesByGame(EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        gameRequestValidation.validateIfGameExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        Long numberofGames = gameDAO.getNumberOfReleasesByGame(request.getEntity());

        return new PayloadResponse<>(request, ResponseCode.OK, numberofGames);
    }

    @Override
    public PagedPayloadResponse<FeatureResponse> getFeaturesByGame(SearchRequest<Long> request) throws ApiException {
        gameRequestValidation.validateIfGameExists(new EntityRequest<>(request.getEntity(), request), VALIDATE_ABSTRACT_REQUEST);

        PagedData<FeatureEntity> entityPagedData = featureDAO.getFeaturesByGame(request.getEntity());
        PagedData<FeatureResponse> featurePagedData = featureMapper.entitiesToDtos(entityPagedData);

        return new PagedPayloadResponse<>(request, ResponseCode.OK, featurePagedData);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<GameFeatureResponse> addFeature(final EntityRequest<GameFeatureCreateRequest> request) throws ApiException {
        featureRequestValidation.validateIfFeatureExists(new EntityRequest<>(request.getEntity().getFeatureId(), request),
                VALIDATE_ABSTRACT_REQUEST);

        gameRequestValidation.validateIfGameExists(new EntityRequest<>(request.getEntity().getGameId(), request),
                VALIDATE_ABSTRACT_REQUEST);

        gameRequestValidation.validateIfGameAlreadyHasFeature(request, VALIDATE_ABSTRACT_REQUEST);

        GameFeatureEntity entity = new GameFeatureEntity();
        final String uuid = UUID.randomUUID().toString();
        entity.setUuid(uuid);
        entity.setGame(gameDAO.findByPK(request.getEntity().getGameId()));
        entity.setFeature(featureDAO.findByPK(request.getEntity().getFeatureId()));
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(request.getUserId());
        gameFeatureDAO.merge(entity);

        return new PayloadResponse<>(request, ResponseCode.OK, gameFeatureMapper.entityToDto(entity));
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> removeFeature(final EntityRequest<String> request) throws ApiException {
        gameRequestValidation.validateIfGameHasFeature(new EntityRequest<>(request.getEntity(), request), VALIDATE_ABSTRACT_REQUEST);

        gameFeatureDAO.removeByPK(request.getEntity());

        return new PayloadResponse<>(request, ResponseCode.OK, "Feature removed!");
    }
}
