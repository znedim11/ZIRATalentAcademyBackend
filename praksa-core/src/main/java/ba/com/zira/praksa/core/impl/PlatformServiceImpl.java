package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.PlatformService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.game.GameDetailResponse;
import ba.com.zira.praksa.api.model.platform.PlatformCreateRequest;
import ba.com.zira.praksa.api.model.platform.PlatformOverviewResponse;
import ba.com.zira.praksa.api.model.platform.PlatformResponse;
import ba.com.zira.praksa.api.model.platform.PlatformUpdateRequest;
import ba.com.zira.praksa.api.model.region.Region;
import ba.com.zira.praksa.core.validation.PlatformRequestValidation;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.PlatformDAO;
import ba.com.zira.praksa.dao.ReleaseDAO;
import ba.com.zira.praksa.dao.model.PlatformEntity;
import ba.com.zira.praksa.dao.model.ReleaseEntity;
import ba.com.zira.praksa.mapper.PlatformMapper;

@Service
public class PlatformServiceImpl implements PlatformService {
    static final String VALIDATE_ABSTRACT_REQUEST = "validateAbstractRequest";
    PlatformRequestValidation platformRequestValidation;
    RequestValidator requestValidator;
    PlatformDAO platformDAO;
    PlatformMapper platformMapper;
    ReleaseDAO releaseDAO;
    GameDAO gameDAO;

    public PlatformServiceImpl(PlatformRequestValidation platformRequestValidation, RequestValidator requestValidator,
            PlatformDAO platformDAO, PlatformMapper platformMapper, ReleaseDAO releaseDAO, GameDAO gameDAO) {
        super();
        this.platformRequestValidation = platformRequestValidation;
        this.requestValidator = requestValidator;
        this.platformDAO = platformDAO;
        this.platformMapper = platformMapper;
        this.releaseDAO = releaseDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public PagedPayloadResponse<PlatformResponse> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<PlatformEntity> platformModelEntities = platformDAO.findAll(request.getFilter());
        final List<PlatformResponse> platformList = new ArrayList<>();

        for (final PlatformEntity PlatformEntity : platformModelEntities.getRecords()) {
            platformList.add(platformMapper.entityToDto(PlatformEntity));
        }
        return new PagedPayloadResponse<>(request, ResponseCode.OK, platformList.size(), 1, 1, platformList.size(), platformList);
    }

    @Override
    public PayloadResponse<PlatformResponse> create(EntityRequest<PlatformCreateRequest> request) throws ApiException {
        requestValidator.validate(request);
        PlatformEntity entity = platformMapper.dtoToEntity(request.getEntity());
        platformDAO.persist(entity);
        PlatformResponse response = platformMapper.entityToDto(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    public PayloadResponse<PlatformResponse> findById(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        final PlatformEntity entity = platformDAO.findByPK(request.getEntity());

        final PlatformResponse response = platformMapper.entityToDto(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Transactional(rollbackFor = ApiException.class)
    @Override
    public PayloadResponse<PlatformResponse> update(final EntityRequest<PlatformUpdateRequest> request) throws ApiException {
        platformRequestValidation.validateUpdatePlatformRequest(request, "update");

        PlatformEntity existingPlatformEntity = platformDAO.findByPK(request.getEntity().getId());

        final LocalDateTime date = LocalDateTime.now();
        final PlatformUpdateRequest platform = request.getEntity();

        platformMapper.updateForPlatformUpdate(platform, existingPlatformEntity);

        existingPlatformEntity.setModified(date);
        existingPlatformEntity.setModifiedBy(request.getUserId());

        platformDAO.merge(existingPlatformEntity);

        final PlatformResponse response = platformMapper.entityToDto(existingPlatformEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);

    }

    @Override
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        platformDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Platform deleted!");
    }

    @Override
    public PagedPayloadResponse<LoV> getLoVs(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<LoV> loVs = platformDAO.getLoVs(request.getFilter());

        return new PagedPayloadResponse<>(request, ResponseCode.OK, loVs);
    }

    @Override
    public PayloadResponse<PlatformOverviewResponse> detail(SearchRequest<Long> request) throws ApiException {
        final List<GameDetailResponse> games = new ArrayList<>();
        final List<Region> regions = new ArrayList<>();
        List<LocalDateTime> dateList = new ArrayList<>();
        int counter = 0;

        final PlatformEntity platform = platformDAO.findByPK(request.getEntity());
        List<ReleaseEntity> releaseModelEntities = releaseDAO.findByPlatformId(platform.getId());

        for (final ReleaseEntity releaseEntity : releaseModelEntities) {
            dateList.add(releaseEntity.getReleaseDate());
            if (releaseEntity.getGame() != null && counter < 5) {
                boolean idFound = false;

                for (final GameDetailResponse Game : games) {
                    if (Game.getId().equals(releaseEntity.getGame().getId())) {
                        idFound = true;
                    }
                }

                if (!idFound) {
                    games.add(setGame(releaseEntity));
                    counter++;
                }

            }
            if (releaseEntity.getRegion() != null) {
                boolean idFound = false;

                for (final Region region : regions) {
                    if (region.getId().equals(releaseEntity.getRegion().getId())) {
                        idFound = true;
                    }
                }

                if (!idFound) {
                    regions.add(setRegion(releaseEntity));
                }
            }
        }

        PlatformOverviewResponse response = setResponse(platform, Collections.max(dateList).toString(),
                Collections.min(dateList).toString(), gameDAO.countAll(), games, regions);

        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    private Region setRegion(ReleaseEntity releaseEntity) {
        Region region = new Region();

        region.setId(releaseEntity.getRegion().getId());
        region.setCode(releaseEntity.getRegion().getName());
        region.setName(releaseEntity.getRegion().getName());
        region.setReleaseDate(releaseEntity.getReleaseDate());

        return region;
    }

    private GameDetailResponse setGame(ReleaseEntity releaseEntity) {
        GameDetailResponse game = new GameDetailResponse();
        game.setId(releaseEntity.getGame().getId());
        game.setName(releaseEntity.getGame().getFullName());
        game.setDeveloperId(releaseEntity.getDeveloper().getId());
        game.setDeveloperName(releaseEntity.getDeveloper().getName());
        game.setPublisherId(releaseEntity.getPublisher().getId());
        game.setPublisherName(releaseEntity.getPublisher().getName());
        game.setReleaseDate(releaseEntity.getReleaseDate());

        return game;
    }

    private PlatformOverviewResponse setResponse(PlatformEntity platform, String latestGameRelease, String firstGameRelease,
            int totalNumOfGames, List<GameDetailResponse> games, List<Region> regions) {
        PlatformOverviewResponse response = new PlatformOverviewResponse();

        response.setId(platform.getId());
        response.setAbbriviation(platform.getAbbriviation());
        response.setCode(platform.getCode());
        response.setFullName(platform.getFullName());
        response.setInformation(platform.getInformation());
        response.setOutlineText(platform.getOutlineText());
        response.setGames(games);
        response.setRegions(regions);

        return response;
    }
}
