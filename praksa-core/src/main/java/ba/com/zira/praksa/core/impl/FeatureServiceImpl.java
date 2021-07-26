package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.FeatureService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.feature.FeatureCreateRequest;
import ba.com.zira.praksa.api.model.feature.FeatureResponse;
import ba.com.zira.praksa.api.model.feature.FeatureUpdateRequest;
import ba.com.zira.praksa.api.model.game.Game;
import ba.com.zira.praksa.core.validation.FeatureRequestValidation;
import ba.com.zira.praksa.dao.FeatureDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.GameFeatureDAO;
import ba.com.zira.praksa.dao.model.FeatureEntity;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.dao.model.GameFeatureEntity;
import ba.com.zira.praksa.mapper.FeatureMapper;
import ba.com.zira.praksa.mapper.GameMapper;

/**
 *
 * @author zira
 *
 */
@Service
public class FeatureServiceImpl implements FeatureService {

    private RequestValidator requestValidator;
    private FeatureRequestValidation featureRequestValidation;
    private FeatureDAO featureDAO;
    private GameDAO gameDAO;
    GameFeatureDAO gameFeatureDAO;
    private FeatureMapper featureMapper;
    private GameMapper gameMapper;

    static final String VALIDATE_ABSTRACT_REQUEST = "validateAbstractRequest";

    public FeatureServiceImpl(RequestValidator requestValidator, FeatureRequestValidation featureRequestValidation, FeatureDAO featureDAO,
            GameDAO gameDAO, GameFeatureDAO gameFeatureDAO, FeatureMapper featureMapper, GameMapper gameMapper) {
        super();
        this.requestValidator = requestValidator;
        this.featureRequestValidation = featureRequestValidation;
        this.featureDAO = featureDAO;
        this.gameDAO = gameDAO;
        this.gameFeatureDAO = gameFeatureDAO;
        this.featureMapper = featureMapper;
        this.gameMapper = gameMapper;
    }

    @Override
    public PagedPayloadResponse<FeatureResponse> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<FeatureEntity> featureEntitiesPaged = featureDAO.findAll(request.getFilter());

        final PagedData<FeatureResponse> featuresPaged = featureMapper.entitiesToDtos(featureEntitiesPaged);

        return new PagedPayloadResponse<>(request, ResponseCode.OK, featuresPaged);
    }

    @Override
    public PayloadResponse<FeatureResponse> findById(final SearchRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        featureRequestValidation.validateIfFeatureExists(entityRequest, VALIDATE_ABSTRACT_REQUEST);

        final FeatureEntity featureEntity = featureDAO.findByPK(request.getEntity());

        final FeatureResponse feature = featureMapper.entityToDto(featureEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, feature);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<FeatureResponse> create(EntityRequest<FeatureCreateRequest> request) throws ApiException {
        featureRequestValidation.validateFeatureRequestFields(request, "basicNotNull");

        FeatureEntity featureEntity = featureMapper.dtoToEntity(request.getEntity());
        featureEntity.setCreated(LocalDateTime.now());
        featureEntity.setCreatedBy(request.getUserId());

        featureDAO.persist(featureEntity);
        FeatureResponse feature = featureMapper.entityToDto(featureEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, feature);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<FeatureResponse> update(final EntityRequest<FeatureUpdateRequest> request) throws ApiException {
        featureRequestValidation.validateBasicFeatureRequest(request);

        final FeatureEntity featureEntity = featureDAO.findByPK(request.getEntity().getId());
        featureMapper.updateDtoToEntity(request.getEntity(), featureEntity);

        featureEntity.setModified(LocalDateTime.now());
        featureEntity.setModifiedBy(request.getUserId());

        featureDAO.merge(featureEntity);
        final FeatureResponse feature = featureMapper.entityToDto(featureEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, feature);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        featureRequestValidation.validateIfFeatureExists(entityRequest, VALIDATE_ABSTRACT_REQUEST);

        featureDAO.deleteRelations(request.getEntity());
        featureDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Feature deleted!");
    }

    @Override
    public PagedPayloadResponse<Game> getGamesByFeature(final EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        featureRequestValidation.validateIfFeatureExists(entityRequest, VALIDATE_ABSTRACT_REQUEST);

        PagedData<GameEntity> entityPagedData = gameDAO.getGamesByFeature(request.getEntity());

        PagedData<Game> gamePagedData = gameMapper.entitiesToDtos(entityPagedData);

        return new PagedPayloadResponse<>(request, ResponseCode.OK, gamePagedData);
    }

    @Override
    public PayloadResponse<Map<String, Set<Game>>> getSetOfGames(final ListRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        List<LoV> featureLoVs = featureDAO.getLoVs(request.getList());

        List<Long> featureIds = featureLoVs.stream().map(LoV::getId).collect(Collectors.toList());

        List<GameFeatureEntity> gameFeatures = gameFeatureDAO.findbyFeatures(featureIds);

        List<String> combinations = getAllCombinations(featureLoVs);

        // Creates Map (Key = GameEntity, Entry = List of Features Names)
        // grouped by GameEntity
        Map<GameEntity, List<String>> gfMapGame = gameFeatures.stream().flatMap(gf -> {
            Map<GameEntity, String> temp = new HashMap<>();
            temp.put(gf.getGame(), gf.getFeature().getName());
            return temp.entrySet().stream();
        }).collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        gfMapGame.forEach((game, features) -> features.sort(null));

        // Converts Entries(List<Strign>) to String(Format: "name1#name2#nameN")
        // and adds it to corresponding game
        Map<GameEntity, String> gfStringMap = new HashMap<>();
        for (Map.Entry<GameEntity, List<String>> game : gfMapGame.entrySet()) {
            StringBuilder temp = new StringBuilder();
            for (String value : gfMapGame.get(game.getKey())) {
                temp.append(value + "#");
            }
            gfStringMap.put(game.getKey(), temp.substring(0, temp.length() - 1));
        }

        Map<String, Set<Game>> gamesMap = new HashMap<>();
        for (String set : combinations) {
            gamesMap.put(set, new HashSet<>());
        }

        for (Map.Entry<GameEntity, String> game : gfStringMap.entrySet()) {
            for (String featureSet : combinations) {
                if (gfStringMap.get(game.getKey()).contains(featureSet)) {
                    gamesMap.get(featureSet).add(gameMapper.entityToDto(game.getKey()));
                }
            }
        }

        return new PayloadResponse<>(request, ResponseCode.OK, gamesMap);
    }

    // Private Help Methods
    private List<String> getAllCombinations(List<LoV> sequence) {
        List<String> result = new ArrayList<>();
        List<String> combinations = new ArrayList<>();

        String[] data = new String[sequence.size()];
        for (int r = 1; r <= sequence.size(); r++) {
            getCombination(sequence, combinations, data, 0, sequence.size() - 1, 0, r);
            result.addAll(combinations);
            combinations.clear();
        }

        return result;
    }

    private void getCombination(List<LoV> sequence, List<String> combinations, String[] data, int start, int end, int index, int r) {
        if (index == r) {
            StringBuilder temp = new StringBuilder();
            for (int j = 0; j < r; j++) {
                temp.append(data[j] + "#");
            }
            combinations.add(temp.substring(0, temp.length() - 1));
        }

        for (int i = start; i <= end && ((end - i + 1) >= (r - index)); i++) {
            data[index] = sequence.get(i).getName();
            getCombination(sequence, combinations, data, i + 1, end, index + 1, r);
        }
    }

    @Override
    public PagedPayloadResponse<LoV> getLoVs(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<LoV> loVs = featureDAO.getLoVs(request.getFilter());

        return new PagedPayloadResponse<>(request, ResponseCode.OK, loVs);
    }

}
