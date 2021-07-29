package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import ba.com.zira.commons.model.Filter;
import ba.com.zira.commons.model.FilterExpression;
import ba.com.zira.commons.model.FilterExpression.FilterOperation;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.commons.model.SortingFilter;
import ba.com.zira.commons.model.SortingFilter.Order;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.FranchiseService;
import ba.com.zira.praksa.api.MediaStoreService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.api.model.franchise.FranchiseCreateRequest;
import ba.com.zira.praksa.api.model.franchise.FranchiseOverviewResponse;
import ba.com.zira.praksa.api.model.franchise.FranchiseResponse;
import ba.com.zira.praksa.api.model.franchise.FranchiseUpdateRequest;
import ba.com.zira.praksa.api.model.game.GameFranchiseResponse;
import ba.com.zira.praksa.api.model.release.ReleaseResponse;
import ba.com.zira.praksa.core.utils.LookupService;
import ba.com.zira.praksa.core.validation.FranchiseRequestValidation;
import ba.com.zira.praksa.core.validation.GameRequestValidation;
import ba.com.zira.praksa.dao.FranchiseDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.ReleaseDAO;
import ba.com.zira.praksa.dao.model.FranchiseEntity;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.mapper.FranchiseMapper;
import ba.com.zira.praksa.mapper.ReleaseMapper;

@Service
public class FranchiseServiceImpl implements FranchiseService {

    private static final String VALIDATENUMBER = "validateNumber";

    private RequestValidator requestValidator;
    private FranchiseDAO franchiseDAO;
    private FranchiseMapper franchiseMapper;
    private FranchiseRequestValidation franchiseRequestValidation;
    private GameDAO gameDAO;
    private GameRequestValidation gameRequestValidation;
    private ReleaseMapper releaseMapper;
    private ReleaseDAO releaseDAO;
    private LookupService lookupService;
    private MediaStoreService mediaStoreService;

    public FranchiseServiceImpl(final RequestValidator requestValidator, FranchiseDAO franchiseDAO, FranchiseMapper franchiseMapper,
            GameDAO gameDAO, ReleaseMapper releaseMapper, ReleaseDAO releaseDAO, FranchiseRequestValidation franchiseRequestValidation,
            GameRequestValidation gameRequestValidation, LookupService lookupService, MediaStoreService mediaStoreService) {
        this.requestValidator = requestValidator;
        this.franchiseDAO = franchiseDAO;
        this.franchiseMapper = franchiseMapper;
        this.gameDAO = gameDAO;
        this.releaseMapper = releaseMapper;
        this.releaseDAO = releaseDAO;
        this.franchiseRequestValidation = franchiseRequestValidation;
        this.gameRequestValidation = gameRequestValidation;
        this.lookupService = lookupService;
        this.mediaStoreService = mediaStoreService;
    }

    @Override
    public PagedPayloadResponse<FranchiseResponse> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<FranchiseEntity> franchiseModelEntities = franchiseDAO.findAll(request.getFilter());
        final List<FranchiseResponse> franchiseList = new ArrayList<>();

        for (final FranchiseEntity FranchiseEntity : franchiseModelEntities.getRecords()) {
            franchiseList.add(franchiseMapper.entityToDto(FranchiseEntity));
        }
        return new PagedPayloadResponse<>(request, ResponseCode.OK, franchiseList.size(), 1, 1, franchiseList.size(), franchiseList);
    }

    @Override
    public PayloadResponse<FranchiseResponse> create(final EntityRequest<FranchiseCreateRequest> request) throws ApiException {
        requestValidator.validate(request);
        FranchiseEntity entity = franchiseMapper.dtoToEntity(request.getEntity());
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(request.getUserId());

        final List<GameEntity> games = new ArrayList<>();
        for (Long gameId : request.getEntity().getGamesIds()) {
            SearchRequest<Long> rq = new SearchRequest<>();
            rq.setEntity(gameId);
            gameRequestValidation.validateGameExist(rq, VALIDATENUMBER);
            games.add(gameDAO.findByPK(gameId));
        }
        entity.setGames(games);
        franchiseDAO.persist(entity);
        FranchiseResponse response = franchiseMapper.entityToDto(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    public PayloadResponse<FranchiseResponse> findById(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        final FranchiseEntity franchiseEntity = franchiseDAO.findByPK(request.getEntity());

        final FranchiseResponse franchise = franchiseMapper.entityToDto(franchiseEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, franchise);
    }

    @Transactional(rollbackFor = ApiException.class)
    @Override
    public PayloadResponse<FranchiseResponse> update(final EntityRequest<FranchiseUpdateRequest> request) throws ApiException {
        requestValidator.validate(request);
        franchiseRequestValidation.validateUpdateFranchiseRequest(new EntityRequest<>(request.getEntity().getId(), request));

        FranchiseEntity existingFranchiseEntity = franchiseDAO.findByPK(request.getEntity().getId());
        final List<GameEntity> games = new ArrayList<>();
        for (GameEntity game : existingFranchiseEntity.getGames()) {
            games.add(game);
        }
        for (Long gameId : request.getEntity().getGamesIds()) {
            SearchRequest<Long> rq = new SearchRequest<>();
            rq.setEntity(gameId);
            gameRequestValidation.validateGameExist(rq, VALIDATENUMBER);
            games.add(gameDAO.findByPK(gameId));
        }

        final LocalDateTime date = LocalDateTime.now();
        final FranchiseUpdateRequest franchise = request.getEntity();
        existingFranchiseEntity.setGames(games);

        franchiseMapper.updateForFranchiseUpdate(franchise, existingFranchiseEntity);

        existingFranchiseEntity.setModified(date);
        existingFranchiseEntity.setModifiedBy(request.getUserId());

        franchiseDAO.merge(existingFranchiseEntity);

        final FranchiseResponse response = franchiseMapper.entityToDto(existingFranchiseEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);

    }

    @Override
    public ListPayloadResponse<LoV> getLoVs(final ListRequest<Long> request) throws ApiException {

        List<LoV> loVs = franchiseDAO.getLoVs(request.getList());

        return new ListPayloadResponse<>(request, ResponseCode.OK, loVs);
    }

    @Override
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        franchiseRequestValidation.validateFranchiseExists(entityRequest, VALIDATENUMBER);

        franchiseDAO.removeByPK(request.getEntity());

        return new PayloadResponse<>(request, ResponseCode.OK, "Franchise deleted!");
    }

    @Override
    public PayloadResponse<FranchiseOverviewResponse> getInformationById(final EntityRequest<Long> request) throws ApiException {
        requestValidator.validate(request);
        franchiseRequestValidation.validateFranchiseExists(request, VALIDATENUMBER);
        final FranchiseEntity franchiseEntity = franchiseDAO.findByPK(request.getEntity());
        FranchiseOverviewResponse response = collectFranchiseDetail(franchiseEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    private FranchiseOverviewResponse collectFranchiseDetail(final FranchiseEntity franchiseEntity) throws ApiException {
        List<GameEntity> gameList = franchiseEntity.getGames();
        List<GameFranchiseResponse> gameResponses = new ArrayList<>();
        List<String> franchisePlatforms = new ArrayList<>();
        List<String> franchiseDevelopers = new ArrayList<>();
        List<String> franchisePublishers = new ArrayList<>();
        LocalDateTime startDate = null;
        LocalDateTime lastReleaseDate = null;
        for (GameEntity gr : gameList) {
            GameFranchiseResponse gtr = new GameFranchiseResponse();
            gtr.setGameId(gr.getId());
            gtr.setGameName(gr.getFullName());
            gtr.setPlatforms(new ArrayList<>());
            gtr.setDevelopers(new ArrayList<>());
            gtr.setPublishers(new ArrayList<>());
            List<ReleaseResponse> gameReleases = getReleaseResponses(gr);
            setGameFranchiseDates(startDate, lastReleaseDate, gameReleases, gtr);
            for (ReleaseResponse release : gameReleases) {
                if (release.getPlatform() == null) {
                    continue;
                } else {
                    String platformAbbriviation = release.getPlatform().getAbbriviation();
                    if (!gtr.getPlatforms().contains(platformAbbriviation)) {
                        gtr.getPlatforms().add(platformAbbriviation);
                    }
                    if (!franchisePlatforms.contains(platformAbbriviation)) {
                        franchisePlatforms.add(platformAbbriviation);
                    }
                }
            }

            for (ReleaseResponse release : gameReleases) {
                if (release.getDeveloper() == null) {
                    continue;
                } else {
                    String developerName = release.getDeveloper().getName();
                    if (!gtr.getDevelopers().contains(developerName)) {
                        gtr.getDevelopers().add(developerName);
                    }
                    if (!franchiseDevelopers.contains(developerName)) {
                        franchiseDevelopers.add(developerName);
                    }
                }
            }

            for (ReleaseResponse release : gameReleases) {
                if (release.getPublisher() == null) {
                    continue;
                } else {
                    String publisherName = release.getPublisher().getName();
                    if (!gtr.getDevelopers().contains(publisherName)) {
                        gtr.getDevelopers().add(publisherName);
                    }
                    if (!franchisePublishers.contains(publisherName)) {
                        franchisePublishers.add(publisherName);
                    }
                }
            }

            gameResponses.add(gtr);
        }

        lookupService.lookupCoverImage(gameResponses, GameFranchiseResponse::getGameId, ObjectType.GAME.getValue(),
                GameFranchiseResponse::setImageUrl, GameFranchiseResponse::getImageUrl);

        return mapFranchiseResponse(franchiseEntity, gameList, gameResponses, franchisePlatforms, franchiseDevelopers, lastReleaseDate,
                startDate, franchisePublishers);
    }

    private void setGameFranchiseDates(LocalDateTime startDate, LocalDateTime lastReleaseDate, List<ReleaseResponse> gameReleases,
            GameFranchiseResponse gtr) {
        if (!gameReleases.isEmpty()) {
            if (startDate == null || gameReleases.get(0).getReleaseDate().isBefore(startDate)) {
                startDate = gameReleases.get(0).getReleaseDate();
            }
            if (lastReleaseDate == null || gameReleases.get(gameReleases.size() - 1).getReleaseDate().isAfter(lastReleaseDate)) {
                lastReleaseDate = gameReleases.get(gameReleases.size() - 1).getReleaseDate();
            }
            if (gtr.getFirstReleaseDate() == null || gameReleases.get(0).getReleaseDate().isBefore(gtr.getFirstReleaseDate())) {
                gtr.setFirstReleaseDate(gameReleases.get(0).getReleaseDate());
            }
        }
    }

    private List<ReleaseResponse> getReleaseResponses(GameEntity gr) {
        Filter filter = new Filter();
        List<FilterExpression> filterExpressions = new ArrayList<>();
        FilterExpression filterExpression = new FilterExpression();
        filterExpression.setAttribute("game");
        filterExpression.setFilterOperation(FilterOperation.EQUALS);
        filterExpression.setExpressionValueObject(gr);
        filterExpressions.add(filterExpression);
        filter.setFilterExpressions(filterExpressions);
        SortingFilter sortingFilter = new SortingFilter("releaseDate", Order.ASCENDING);
        List<SortingFilter> sortingFilters = new ArrayList<>();
        sortingFilters.add(sortingFilter);
        filter.setSortingFilters(sortingFilters);
        return releaseMapper.entitiesToDtos(releaseDAO.findAll(filter).getRecords());
    }

    private FranchiseOverviewResponse mapFranchiseResponse(final FranchiseEntity franchiseEntity, List<GameEntity> gameList,
            List<GameFranchiseResponse> gameResponse, List<String> franchiseAbbriviation, List<String> franchiseDevelopers,
            LocalDateTime lastReleaseDate, LocalDateTime startDate, List<String> franchisePublishers) {
        FranchiseOverviewResponse response = new FranchiseOverviewResponse();
        response.setFranchiseId(franchiseEntity.getId());
        response.setFranchiseName(franchiseEntity.getName());
        response.setCreated(franchiseEntity.getCreated());
        response.setCreatedBy(franchiseEntity.getCreatedBy());
        response.setInformation(franchiseEntity.getInformation());
        response.setOutineText(franchiseEntity.getOutlineText());
        response.setAliases(franchiseEntity.getAliases());
        response.setStartDate(startDate);
        response.setLastReleaseDate(lastReleaseDate);
        response.setNumberOfGames(Long.valueOf(gameList.size()));
        response.setNumberOfPlatforms(Long.valueOf(franchiseAbbriviation.size()));
        response.setNumberOfDevelopers(Long.valueOf(franchiseDevelopers.size()));
        response.setNumberOfPublishers(Long.valueOf(franchisePublishers.size()));
        response.setGames(gameResponse);
        return response;
    }
}
