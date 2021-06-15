package ba.com.zira.praksa.core.impl;

import java.util.ArrayList;
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
import ba.com.zira.praksa.api.GameService;
import ba.com.zira.praksa.api.model.game.Game;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.mapper.GameMapper;

@Service
public class GameServiceImpl implements GameService {

    RequestValidator requestValidator;
    GameDAO gameDAO;
    GameMapper gameMapper;

    public GameServiceImpl(RequestValidator requestValidator, GameDAO gameDAO, GameMapper gameMapper) {
        super();
        this.requestValidator = requestValidator;
        this.gameDAO = gameDAO;
        this.gameMapper = gameMapper;
    }

    @Override
    public PagedPayloadResponse<Game> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<GameEntity> gameModelEntities = gameDAO.findAll(request.getFilter());
        final List<Game> gameList = new ArrayList<>();

        for (final GameEntity GameEntity : gameModelEntities.getRecords()) {
            gameList.add(gameMapper.entityToDto(GameEntity));
        }
        return new PagedPayloadResponse<>(request, ResponseCode.OK, gameList.size(), 1, 1, gameList.size(), gameList);
    }

    @Override
    public PayloadResponse<Game> create(EntityRequest<Game> request) throws ApiException {
        requestValidator.validate(request);
        GameEntity entity = gameMapper.dtoToEntity(request.getEntity());
        gameDAO.persist(entity);
        Game response = gameMapper.entityToDto(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    public PayloadResponse<Game> findById(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        final GameEntity gameEntity = gameDAO.findByPK(request.getEntity());

        final Game game = gameMapper.entityToDto(gameEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, game);
    }

    @Transactional(rollbackFor = ApiException.class)
    @Override
    public PayloadResponse<Game> update(final EntityRequest<Game> request) throws ApiException {
        requestValidator.validate(request);

        final Game game = request.getEntity();
        final GameEntity gameEntity = gameMapper.dtoToEntity(game);

        gameDAO.merge(gameEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, game);

    }

    @Override
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        gameDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Game deleted!");
    }
}
