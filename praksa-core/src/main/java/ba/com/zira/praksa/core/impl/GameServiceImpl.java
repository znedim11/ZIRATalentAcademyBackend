package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import ba.com.zira.praksa.api.GameService;
import ba.com.zira.praksa.api.model.feature.FeatureResponse;
import ba.com.zira.praksa.api.model.game.GameCreateRequest;
import ba.com.zira.praksa.api.model.game.GameResponse;
import ba.com.zira.praksa.api.model.game.GameUpdateRequest;
import ba.com.zira.praksa.api.model.gamefeature.GameFeatureCreateRequest;
import ba.com.zira.praksa.api.model.gamefeature.GameFeatureResponse;
import ba.com.zira.praksa.core.validation.FeatureRequestValidation;
import ba.com.zira.praksa.core.validation.GameRequestValidation;
import ba.com.zira.praksa.dao.FeatureDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.GameFeatureDAO;
import ba.com.zira.praksa.dao.model.FeatureEntity;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.dao.model.GameFeatureEntity;
import ba.com.zira.praksa.mapper.FeatureMapper;
import ba.com.zira.praksa.mapper.GameFeatureMapper;
import ba.com.zira.praksa.mapper.GameMapper;

@Service
public class GameServiceImpl implements GameService {

    RequestValidator requestValidator;
    GameRequestValidation gameRequestValidation;
    FeatureRequestValidation featureRequestValidation;
    GameDAO gameDAO;
    FeatureDAO featureDAO;
    GameFeatureDAO gameFeatureDAO;
    GameMapper gameMapper;
    FeatureMapper featureMapper;
    GameFeatureMapper gameFeatureMapper;

    public GameServiceImpl(RequestValidator requestValidator, GameRequestValidation gameRequestValidation,
            FeatureRequestValidation featureRequestValidation, GameDAO gameDAO, FeatureDAO featureDAO, GameFeatureDAO gameFeatureDAO,
            GameMapper gameMapper, FeatureMapper featureMapper, GameFeatureMapper gameFeatureMapper) {
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
    public PagedPayloadResponse<FeatureResponse> getFeaturesByGame(SearchRequest<Long> request) throws ApiException {
        gameRequestValidation.validateIfGameExists(new EntityRequest<>(request.getEntity(), request), "validateAbstractRequest");

        PagedData<FeatureEntity> entityPagedData = featureDAO.getFeaturesByGame(request.getEntity());
        PagedData<FeatureResponse> featurePagedData = featureMapper.entitiesToDtos(entityPagedData);

        return new PagedPayloadResponse<>(request, ResponseCode.OK, featurePagedData);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<GameFeatureResponse> addFeature(final EntityRequest<GameFeatureCreateRequest> request) throws ApiException {
        featureRequestValidation.validateIfFeatureExists(new EntityRequest<>(request.getEntity().getFeatureId(), request),
                "validateAbstractRequest");

        gameRequestValidation.validateIfGameExists(new EntityRequest<>(request.getEntity().getGameId(), request),
                "validateAbstractRequest");

        gameRequestValidation.validateIfGameAlreadyHasFeature(request, "validateAbstractRequest");

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
        gameRequestValidation.validateIfGameHasFeature(new EntityRequest<>(request.getEntity(), request), "validateAbstractRequest");

        gameFeatureDAO.removeByPK(request.getEntity());

        return new PayloadResponse<>(request, ResponseCode.OK, "Feature removed!");
    }
}
