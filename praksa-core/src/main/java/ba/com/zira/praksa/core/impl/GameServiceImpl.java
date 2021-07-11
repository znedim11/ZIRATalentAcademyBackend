package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EmptyRequest;
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
import ba.com.zira.praksa.api.model.game.GameOverviewResponse;
import ba.com.zira.praksa.api.model.game.GameResponse;
import ba.com.zira.praksa.api.model.game.GameUpdateRequest;
import ba.com.zira.praksa.api.model.game.dlc.DlcAnalysisReport;
import ba.com.zira.praksa.api.model.gamefeature.GameFeatureCreateRequest;
import ba.com.zira.praksa.api.model.gamefeature.GameFeatureResponse;
import ba.com.zira.praksa.api.model.location.Location;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.api.model.person.Person;
import ba.com.zira.praksa.api.model.platform.PlatformResponse;
import ba.com.zira.praksa.api.model.release.ReleaseResponseLight;
import ba.com.zira.praksa.core.validation.FeatureRequestValidation;
import ba.com.zira.praksa.core.validation.GameRequestValidation;
import ba.com.zira.praksa.dao.CompanyDAO;
import ba.com.zira.praksa.dao.FeatureDAO;
import ba.com.zira.praksa.dao.FranchiseDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.GameFeatureDAO;
import ba.com.zira.praksa.dao.PlatformDAO;
import ba.com.zira.praksa.dao.ReleaseDAO;
import ba.com.zira.praksa.dao.model.CharacterEntity;
import ba.com.zira.praksa.dao.model.ConceptEntity;
import ba.com.zira.praksa.dao.model.FeatureEntity;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.dao.model.GameFeatureEntity;
import ba.com.zira.praksa.dao.model.LocationEntity;
import ba.com.zira.praksa.dao.model.ObjectEntity;
import ba.com.zira.praksa.dao.model.PersonEntity;
import ba.com.zira.praksa.dao.model.ReleaseEntity;
import ba.com.zira.praksa.mapper.CharacterMapper;
import ba.com.zira.praksa.mapper.ConceptMapper;
import ba.com.zira.praksa.mapper.FeatureMapper;
import ba.com.zira.praksa.mapper.GameFeatureMapper;
import ba.com.zira.praksa.mapper.GameMapper;
import ba.com.zira.praksa.mapper.LocationMapper;
import ba.com.zira.praksa.mapper.ObjectMapper;
import ba.com.zira.praksa.mapper.PersonMapper;
import ba.com.zira.praksa.mapper.PlatformMapper;
import ba.com.zira.praksa.mapper.ReleaseMapper;

@Service
public class GameServiceImpl implements GameService {

    static final String VALIDATE_ABSTRACT_REQUEST = "validateAbstractRequest";

    RequestValidator requestValidator;
    GameRequestValidation gameRequestValidation;
    FeatureRequestValidation featureRequestValidation;

    GameDAO gameDAO;
    FeatureDAO featureDAO;
    GameFeatureDAO gameFeatureDAO;
    ReleaseDAO releaseDAO;
    PlatformDAO platformDAO;
    CompanyDAO companyDAO;
    FranchiseDAO franchiseDAO;

    GameMapper gameMapper;
    ConceptMapper conceptMapper;
    PersonMapper personMapper;
    ObjectMapper objectMapper;
    CharacterMapper characterMapper;
    LocationMapper locationMapper;
    FeatureMapper featureMapper;
    GameFeatureMapper gameFeatureMapper;
    ReleaseMapper releaseMapper;
    PlatformMapper platformMapper;

    public GameServiceImpl(RequestValidator requestValidator, GameRequestValidation gameRequestValidation,
            FeatureRequestValidation featureRequestValidation, GameDAO gameDAO, FeatureDAO featureDAO, GameFeatureDAO gameFeatureDAO,
            ReleaseDAO releaseDAO, PlatformDAO platformDAO, CompanyDAO companyDAO, FranchiseDAO franchiseDAO, GameMapper gameMapper,
            ConceptMapper conceptMapper, PersonMapper personMapper, ObjectMapper objectMapper, CharacterMapper characterMapper,
            LocationMapper locationMapper, FeatureMapper featureMapper, GameFeatureMapper gameFeatureMapper, ReleaseMapper releaseMapper,
            PlatformMapper platformMapper) {
        super();
        this.requestValidator = requestValidator;
        this.gameRequestValidation = gameRequestValidation;
        this.featureRequestValidation = featureRequestValidation;
        this.gameDAO = gameDAO;
        this.featureDAO = featureDAO;
        this.gameFeatureDAO = gameFeatureDAO;
        this.releaseDAO = releaseDAO;
        this.platformDAO = platformDAO;
        this.companyDAO = companyDAO;
        this.franchiseDAO = franchiseDAO;
        this.gameMapper = gameMapper;
        this.conceptMapper = conceptMapper;
        this.personMapper = personMapper;
        this.objectMapper = objectMapper;
        this.characterMapper = characterMapper;
        this.locationMapper = locationMapper;
        this.featureMapper = featureMapper;
        this.gameFeatureMapper = gameFeatureMapper;
        this.releaseMapper = releaseMapper;
        this.platformMapper = platformMapper;
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
        if (request.getList() != null) {
            for (Long item : request.getList()) {
                EntityRequest<Long> longRequest = new EntityRequest<>(item, request);
                gameRequestValidation.validateIfGameExists(longRequest, VALIDATE_ABSTRACT_REQUEST);
            }
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

    @Override
    public PayloadResponse<GameOverviewResponse> getOverview(EntityRequest<Long> request) throws ApiException {
        GameOverviewResponse gameOverview = gameMapper.entityToOverviewResponse(gameDAO.findByPK(request.getEntity()));

        List<PlatformResponse> platforms = platformMapper.entityListToDtoList(gameDAO.getPlatformsByGame(request.getEntity()));
        List<ReleaseEntity> entity = gameDAO.getFirstReleaseByGame(request.getEntity());

        if (!entity.isEmpty()) {
            ReleaseResponseLight release = releaseMapper.releaseEntityToRelease(entity.get(0));
            release.setDeveloperName(entity.get(0).getDeveloper().getName());
            release.setPublisherName(entity.get(0).getPublisher().getName());
            release.setPlatformName(entity.get(0).getPlatform().getFullName());
            gameOverview.setPlatformAbbreviations(platforms.stream().map(PlatformResponse::getAbbriviation).toArray(String[]::new));
            gameOverview.setFirstReleaseDate(release.getReleaseDate().toString());
            gameOverview.setPublisher(release.getPublisherName());
            gameOverview.setDeveloper(release.getDeveloperName());
            gameOverview.setPlatformName(release.getPlatformName());
        }

        return new PayloadResponse<>(request, ResponseCode.OK, gameOverview);
    }

    @Override
    public PayloadResponse<DlcAnalysisReport> dlcAnalysisReport(final EmptyRequest request) throws ApiException {

        DlcAnalysisReport dlcAnalysisReport = new DlcAnalysisReport();
        dlcAnalysisReport.setDlcGames(gameDAO.getDlcGames());
        dlcAnalysisReport.setDlcPlatforms(platformDAO.getDlcPlatforms());
        dlcAnalysisReport.setDlcCompanies(companyDAO.getDlcCompanies());
        dlcAnalysisReport.setDlcFranchises(franchiseDAO.getDlcFranchises());
        dlcAnalysisReport.setTotalNumberOfDlc(gameDAO.getCountOfDlcs());

        return new PayloadResponse<>(request, ResponseCode.OK, dlcAnalysisReport);
    }

}
