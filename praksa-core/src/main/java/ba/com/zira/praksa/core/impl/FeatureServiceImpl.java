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
import ba.com.zira.commons.message.response.ListPayloadResponse;
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
        featureRequestValidation.validateIfFeatureExists(entityRequest, "validateAbstractRequest");

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
        featureRequestValidation.validateIfFeatureExists(entityRequest, "validateAbstractRequest");

        featureDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Feature deleted!");
    }

    @Override
    public ListPayloadResponse<Game> getGamesByFeature(final EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        featureRequestValidation.validateIfFeatureExists(entityRequest, "validateAbstractRequest");

        List<GameEntity> entityList = gameDAO.getGamesByFeature(request.getEntity());
        List<Game> gameList = gameMapper.entityListToDtoList(entityList);

        return new ListPayloadResponse<>(request, ResponseCode.OK, gameList);
    }

    @Override
    public PayloadResponse<Map<String, Set<Game>>> getSetOfGames(final ListRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        // Dobije postojece feature-e
        List<LoV> featureLoVs = featureDAO.getLoVs(request.getList());

        // Izvuce id-eve u zasebnu listu
        List<Long> featureIds = new ArrayList<Long>();
        for (LoV f : featureLoVs) {
            featureIds.add(f.getId());
        }

        // Pokupi GemeFeature relacija na osnovu prethodne liste Feature id-eva
        List<GameFeatureEntity> gameFeatures = gameFeatureDAO.findbyFeatures(featureIds);

        // Kreira sve moguce kombinacije na osnovi liste Feature LoV
        // Format svake kombinacije je
        // "[nazivFeture-a]#[nazivFeture-a]#[nazivFeture-a]"
        List<String> combinations = GetAllCombinations(featureLoVs);

        Map<String, Set<Game>> gamesMap = new HashMap<>();

        // Kreira Map sa grupisanim GameFeature vezama po Game-ovima
        Map<GameEntity, List<GameFeatureEntity>> GFMapGame = gameFeatures.stream()
                .collect(Collectors.groupingBy(GameFeatureEntity::getGame));

        // Kreira Set-ove za svaku kombinaciju
        // Add grouping sets
        for (String set : combinations) {
            gamesMap.put(set, new HashSet<>());
        }

        // Mapiranje ?
        // String "Local Co-oP#Split screen#VR support"
        // Games
        // Game 1: GameFeatures as Features names [ Local Co-oP, VR support ]
        // Game 2: GameFeatures as Features names [ Split screen, VR support ]
        // Game 3: GameFeatures as Features names [ Local Co-oP, Split screen,
        // VR support ]
        // Game 4: GameFeatures as Features names [ Local Co-oP ]

        // Kako vratiti Map kao response, da se ispise npr. naziv set-a i
        // njegovi pripadnici
        return new PayloadResponse<>(request, ResponseCode.OK, gamesMap);
    }

    // Private Help Methods
    private List<String> GetAllCombinations(List<LoV> sequence) {
        List<String> result = new ArrayList<String>();
        List<String> combinations = new ArrayList<String>();

        String[] data = new String[sequence.size()];
        for (int r = 1; r <= sequence.size(); r++) {
            GetCombination(sequence, combinations, data, 0, sequence.size() - 1, 0, r);
            result.addAll(combinations);
            combinations.clear();
        }

        return result;
    }

    private void GetCombination(List<LoV> sequence, List<String> combinations, String[] data, int start, int end, int index, int r) {
        if (index == r) {
            String temp = "";
            for (int j = 0; j < r; j++) {
                temp += data[j] + "#";
            }

            combinations.add(temp.substring(0, temp.length() - 1));
        }

        for (int i = start; i <= end && ((end - i + 1) >= (r - index)); i++) {
            data[index] = sequence.get(i).getName();
            GetCombination(sequence, combinations, data, i + 1, end, index + 1, r);
        }
    }

}
