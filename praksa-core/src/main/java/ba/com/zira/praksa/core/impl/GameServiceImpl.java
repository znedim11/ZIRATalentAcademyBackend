package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EmptyRequest;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.GameService;
import ba.com.zira.praksa.api.MediaService;
import ba.com.zira.praksa.api.MediaStoreService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.character.CharacterResponse;
import ba.com.zira.praksa.api.model.concept.ConceptResponse;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.api.model.feature.FeatureResponse;
import ba.com.zira.praksa.api.model.game.GameCreateRequest;
import ba.com.zira.praksa.api.model.game.GameOverviewResponse;
import ba.com.zira.praksa.api.model.game.GameResponse;
import ba.com.zira.praksa.api.model.game.GameSearchRequest;
import ba.com.zira.praksa.api.model.game.GameUpdateRequest;
import ba.com.zira.praksa.api.model.game.dlc.DlcAnalysisReport;
import ba.com.zira.praksa.api.model.gamefeature.GameFeatureCreateRequest;
import ba.com.zira.praksa.api.model.gamefeature.GameFeatureResponse;
import ba.com.zira.praksa.api.model.location.Location;
import ba.com.zira.praksa.api.model.media.CreateMediaRequest;
import ba.com.zira.praksa.api.model.media.MediaRetrivalRequest;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.api.model.person.Person;
import ba.com.zira.praksa.api.model.platform.PlatformResponse;
import ba.com.zira.praksa.api.model.release.ReleaseResponseLight;
import ba.com.zira.praksa.core.utils.LookupService;
import ba.com.zira.praksa.core.validation.FeatureRequestValidation;
import ba.com.zira.praksa.core.validation.GameRequestValidation;
import ba.com.zira.praksa.dao.CompanyDAO;
import ba.com.zira.praksa.dao.FeatureDAO;
import ba.com.zira.praksa.dao.FranchiseDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.GameFeatureDAO;
import ba.com.zira.praksa.dao.MediaDAO;
import ba.com.zira.praksa.dao.MediaStoreDAO;
import ba.com.zira.praksa.dao.PlatformDAO;
import ba.com.zira.praksa.dao.ReleaseDAO;
import ba.com.zira.praksa.dao.model.CharacterEntity;
import ba.com.zira.praksa.dao.model.CharacterEntity_;
import ba.com.zira.praksa.dao.model.ConceptEntity;
import ba.com.zira.praksa.dao.model.ConceptEntity_;
import ba.com.zira.praksa.dao.model.FeatureEntity;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.dao.model.GameFeatureEntity;
import ba.com.zira.praksa.dao.model.LinkMapEntity_;
import ba.com.zira.praksa.dao.model.LocationEntity;
import ba.com.zira.praksa.dao.model.LocationEntity_;
import ba.com.zira.praksa.dao.model.MediaStoreEntity;
import ba.com.zira.praksa.dao.model.ObjectEntity;
import ba.com.zira.praksa.dao.model.ObjectEntity_;
import ba.com.zira.praksa.dao.model.PersonEntity;
import ba.com.zira.praksa.dao.model.PersonEntity_;
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
    MediaStoreDAO mediaStoreDAO;
    MediaDAO mediaDAO;
    LookupService lookupService;

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

    MediaStoreService mediaStoreService;
    MediaService mediaService;

    public GameServiceImpl(RequestValidator requestValidator, GameRequestValidation gameRequestValidation,
            FeatureRequestValidation featureRequestValidation, GameDAO gameDAO, FeatureDAO featureDAO, GameFeatureDAO gameFeatureDAO,
            ReleaseDAO releaseDAO, PlatformDAO platformDAO, CompanyDAO companyDAO, FranchiseDAO franchiseDAO, MediaStoreDAO mediaStoreDAO,
            MediaDAO mediaDAO, LookupService lookupService, GameMapper gameMapper, ConceptMapper conceptMapper, PersonMapper personMapper,
            ObjectMapper objectMapper, CharacterMapper characterMapper, LocationMapper locationMapper, FeatureMapper featureMapper,
            GameFeatureMapper gameFeatureMapper, ReleaseMapper releaseMapper, PlatformMapper platformMapper,
            MediaStoreService mediaStoreService, MediaService mediaService) {
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
        this.mediaStoreDAO = mediaStoreDAO;
        this.mediaDAO = mediaDAO;
        this.lookupService = lookupService;
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
        this.mediaStoreService = mediaStoreService;
        this.mediaService = mediaService;
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
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<GameResponse> create(EntityRequest<GameCreateRequest> request) throws ApiException {
        requestValidator.validate(request);

        GameCreateRequest requestEntity = request.getEntity();
        GameEntity entity = gameMapper.dtoToEntity(requestEntity);
        if (requestEntity.getDlcGameId() != null) {
            GameEntity parentGame = gameDAO.findByPK(requestEntity.getDlcGameId());
            entity.setParentGame(parentGame);
        }

        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(request.getUserId());

        GameEntity createdEntity = gameDAO.persist(entity);

        if (requestEntity.getImageCreateRequest() != null && requestEntity.getImageCreateRequest().getImageData() != null
                && requestEntity.getImageCreateRequest().getImageName() != null) {
            CreateMediaRequest mediaRequest = new CreateMediaRequest(ObjectType.GAME.getValue(), createdEntity.getId(),
                    requestEntity.getImageCreateRequest().getImageData(), requestEntity.getImageCreateRequest().getImageName(), "IMAGE",
                    "COVER_IMAGE");

            mediaService.saveMedia(new EntityRequest<>(mediaRequest, request));
        }

        GameResponse response = gameMapper.gameEntityToGame(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    public PayloadResponse<GameResponse> findById(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        final GameEntity gameEntity = gameDAO.findByPK(request.getEntity());
        final GameResponse gameResponse = gameMapper.gameEntityToGame(gameEntity);

        MediaRetrivalRequest mrr = new MediaRetrivalRequest();
        mrr.setObjectId(gameResponse.getId());
        mrr.setObjectType(ObjectType.GAME.getValue());
        mrr.setMediaType("COVER_IMAGE");
        gameResponse.setImageUrl(mediaStoreService.getImageUrl(new EntityRequest<>(mrr, request)).getPayload().get(0));

        if (gameEntity.getFranchise() != null) {
            gameResponse.setFranchiseId(gameEntity.getFranchise().getId());
        }
        if (gameEntity.getDlc() != null) {
            if (gameEntity.getDlc().equals("1")) {
                gameResponse.setDlcGameId(gameEntity.getParentGame().getId());
            }
        }

        return new PayloadResponse<>(request, ResponseCode.OK, gameResponse);
    }

    @Transactional(rollbackFor = ApiException.class)
    @Override
    public PayloadResponse<GameResponse> update(final EntityRequest<GameUpdateRequest> request) throws ApiException {
        requestValidator.validate(request);

        final GameUpdateRequest gameRequest = request.getEntity();
        GameEntity gameEntity = gameDAO.findByPK(gameRequest.getId());
        gameMapper.updateForGameUpdate(gameRequest, gameEntity);

        gameEntity.setModified(LocalDateTime.now());
        gameEntity.setModifiedBy(request.getUserId());

        MediaStoreEntity mse = gameDAO.getCoverByGame(request.getEntity().getId());

        if (mse != null) {
            Long mediaId = mse.getMedia().getId();
            mediaStoreDAO.remove(mse);
            mediaDAO.removeByPK(mediaId);
        }

        if (gameRequest.getImageCreateRequest() != null && gameRequest.getImageCreateRequest().getImageData() != null
                && gameRequest.getImageCreateRequest().getImageName() != null) {
            CreateMediaRequest mediaRequest = new CreateMediaRequest(ObjectType.GAME.getValue(), gameEntity.getId(),
                    gameRequest.getImageCreateRequest().getImageData(), gameRequest.getImageCreateRequest().getImageName(), "IMAGE",
                    "COVER_IMAGE");

            mediaService.saveMedia(new EntityRequest<>(mediaRequest, request));
        }

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
    public PagedPayloadResponse<LoV> getLoVs(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<LoV> loVs = gameDAO.getLoVs(request.getFilter());

        return new PagedPayloadResponse<>(request, ResponseCode.OK, loVs);
    }

    @Override
    public ListPayloadResponse<LoV> getMainGames(EmptyRequest request) throws ApiException {

        List<LoV> loVs = gameDAO.getMainGames();

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

        MediaRetrivalRequest mrr = new MediaRetrivalRequest();
        mrr.setObjectId(gameOverview.getId());
        mrr.setObjectType(ObjectType.GAME.getValue());
        mrr.setMediaType("COVER_IMAGE");
        gameOverview.setImageUrl(mediaStoreService.getImageUrl(new EntityRequest<>(mrr, request)).getPayload().get(0));

        if (!entity.isEmpty()) {
            ReleaseResponseLight release = releaseMapper.releaseEntityToRelease(entity.get(0));
            if (entity.get(0).getDeveloper() != null) {
                release.setDeveloperName(entity.get(0).getDeveloper().getName());
            }
            if (entity.get(0).getPublisher() != null) {
                release.setPublisherName(entity.get(0).getPublisher().getName());
            }
            if (entity.get(0).getPlatform() != null) {
                release.setPlatformName(entity.get(0).getPlatform().getFullName());
            }
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
        dlcAnalysisReport.setDlcGames(gameDAO.getDlcGames("0"));
        dlcAnalysisReport.setDlcPlatforms(platformDAO.getDlcPlatforms("1"));
        dlcAnalysisReport.setDlcCompanies(companyDAO.getDlcCompanies("1"));
        dlcAnalysisReport.setDlcFranchises(franchiseDAO.getDlcFranchises("0"));
        dlcAnalysisReport.setTotalNumberOfDlc(gameDAO.getCountOfDlcs());

        return new PayloadResponse<>(request, ResponseCode.OK, dlcAnalysisReport);
    }

    @Override
    public PagedPayloadResponse<LoV> getLoVsNotConnectedTo(final SearchRequest<LoV> request) throws ApiException {
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
        } else if (ObjectType.LOCATION.getValue().equalsIgnoreCase(request.getEntity().getName())) {
            field = LinkMapEntity_.location.getName();
            idField = LocationEntity_.id.getName();
        } else if (ObjectType.OBJECT.getValue().equalsIgnoreCase(request.getEntity().getName())) {
            field = LinkMapEntity_.object.getName();
            idField = ObjectEntity_.id.getName();
        } else if (ObjectType.PERSON.getValue().equalsIgnoreCase(request.getEntity().getName())) {
            field = LinkMapEntity_.person.getName();
            idField = PersonEntity_.id.getName();
        }

        PagedData<LoV> loVs = null;
        if (field != null) {
            loVs = gameDAO.getLoVsNotConnectedTo(request.getFilter(), field, idField, id);
        }

        return new PagedPayloadResponse<>(request, ResponseCode.OK, loVs);
    }

    @Override
    public PagedPayloadResponse<GameResponse> searchGames(SearchRequest<GameSearchRequest> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<GameEntity> gameEntites = gameDAO.searchGames(request.getFilter(), request.getEntity());
        List<GameResponse> gameList = gameMapper.gameEntitesToGames(gameEntites.getRecords());
        lookupService.lookupCoverImage(gameList, GameResponse::getId, ObjectType.GAME.getValue(), GameResponse::setImageUrl,
                GameResponse::getImageUrl);

        PagedData<GameResponse> games = new PagedData<>();
        games.setNumberOfPages(gameEntites.getNumberOfPages());
        games.setNumberOfRecords(gameEntites.getNumberOfRecords());
        games.setPage(gameEntites.getPage());
        games.setRecords(gameList);
        games.setRecordsPerPage(gameEntites.getRecordsPerPage());

        return new PagedPayloadResponse<>(request, ResponseCode.OK, games);
    }

    @Override
    public ListPayloadResponse<String> getGenres(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        List<String> genres = gameDAO.getGenres();
        Map<String, String> genresMap = new HashMap<>();

        for (String genre : genres) {
            Map<String, String> namesMap = Arrays.stream(genre.trim().split("\\s*;\\s*")).filter(g -> g.length() > 0).sorted()
                    .collect(Collectors.toMap(g -> g, g -> g));
            genresMap.putAll(namesMap);
        }

        List<String> genresList = new ArrayList<>(genresMap.keySet());

        return new ListPayloadResponse<>(request, ResponseCode.OK, genresList.stream().sorted().collect(Collectors.toList()));
    }

}
