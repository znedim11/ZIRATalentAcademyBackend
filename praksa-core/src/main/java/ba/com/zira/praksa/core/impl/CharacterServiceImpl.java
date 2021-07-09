package ba.com.zira.praksa.core.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.CharacterService;
import ba.com.zira.praksa.api.MediaService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.character.CharacterCreateRequest;
import ba.com.zira.praksa.api.model.character.CharacterSearchRequest;
import ba.com.zira.praksa.api.model.character.CharacterSearchResponse;
import ba.com.zira.praksa.api.model.character.CharacterUpdateRequest;
import ba.com.zira.praksa.api.model.character.CompleteCharacterResponse;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.api.model.game.GameCharacterResponse;
import ba.com.zira.praksa.api.model.media.CreateMediaRequest;
import ba.com.zira.praksa.core.utils.LookupService;
import ba.com.zira.praksa.core.validation.CharacterRequestValidation;
import ba.com.zira.praksa.dao.CharacterDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.LinkMapDAO;
import ba.com.zira.praksa.dao.model.CharacterEntity;
import ba.com.zira.praksa.dao.model.LinkMapEntity;
import ba.com.zira.praksa.mapper.CharacterMapper;

/**
 * @author zira
 *
 */

@Service
public class CharacterServiceImpl implements CharacterService {

    static final String VALIDATE_ABSTRACT_REQUEST = "validateAbstractRequest";
    static final String BASIC_NOT_NULL = "basicNotNull";

    private RequestValidator requestValidator;
    CharacterRequestValidation characterRequestValidation;
    CharacterDAO characterDAO;
    GameDAO gameDAO;
    LinkMapDAO linkMapDAO;
    CharacterMapper characterMapper;
    MediaService mediaService;
    LookupService lookupService;

    public CharacterServiceImpl(RequestValidator requestValidator, CharacterRequestValidation characterRequestValidation,
            CharacterDAO characterDAO, GameDAO gameDAO, CharacterMapper characterMapper, MediaService mediaService, LinkMapDAO linkMapDAO,
            LookupService lookupService) {
        super();
        this.requestValidator = requestValidator;
        this.characterRequestValidation = characterRequestValidation;
        this.characterDAO = characterDAO;
        this.gameDAO = gameDAO;
        this.linkMapDAO = linkMapDAO;
        this.characterMapper = characterMapper;
        this.mediaService = mediaService;
        this.lookupService = lookupService;
    }

    @Override
    public PagedPayloadResponse<CharacterSearchResponse> searchCharacters(final EntityRequest<CharacterSearchRequest> request)
            throws ApiException {
        requestValidator.validate(request);

        CharacterSearchRequest searchRequest = request.getEntity();

        final List<CharacterSearchResponse> characterList = characterDAO.searchCharacters(searchRequest);

        lookupService.lookupCoverImage(characterList, CharacterSearchResponse::getId, ObjectType.CHARACTER.getValue(),
                CharacterSearchResponse::setImageUrl);

        return new PagedPayloadResponse<>(request, ResponseCode.OK, characterList.size(), 1, 1, characterList.size(), characterList);
    }

    @Override
    public PayloadResponse<CompleteCharacterResponse> findById(SearchRequest<Long> request) throws ApiException {
        characterRequestValidation.validateCharacterExists(new EntityRequest<>(request.getEntity(), request), VALIDATE_ABSTRACT_REQUEST);

        CompleteCharacterResponse characterResponse = characterMapper
                .characterEntityToCompleteCharacter(characterDAO.findByPK(request.getEntity()));

        List<CompleteCharacterResponse> temp = new ArrayList<>();
        temp.add(characterResponse);
        lookupService.lookupCoverImage(temp, CompleteCharacterResponse::getId, ObjectType.CHARACTER.getValue(),
                CompleteCharacterResponse::setImageUrl);

        return new PayloadResponse<>(request, ResponseCode.OK, temp.get(0));
    }

    @Override
    public PagedPayloadResponse<GameCharacterResponse> getGamesForCharacter(final EntityRequest<Long> request) throws ApiException {
        characterRequestValidation.validateCharacterExists(new EntityRequest<>(request.getEntity(), request), VALIDATE_ABSTRACT_REQUEST);

        List<GameCharacterResponse> gamesList = gameDAO.getGamesForCharacter(request.getEntity());

        return new PagedPayloadResponse<>(request, ResponseCode.OK, gamesList.size(), 1, 1, gamesList.size(), gamesList);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<CompleteCharacterResponse> create(EntityRequest<CharacterCreateRequest> request) throws ApiException {
        CharacterCreateRequest requestEntity = request.getEntity();

        CharacterEntity characterEntity = characterMapper.dtoToEntity(requestEntity);
        characterEntity.setCreated(LocalDateTime.now());
        characterEntity.setCreatedBy(request.getUserId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        characterEntity.setDob(requestEntity.getDob() != null ? LocalDate.parse(requestEntity.getDob(), formatter).atStartOfDay() : null);
        characterEntity.setDod(requestEntity.getDod() != null ? LocalDate.parse(requestEntity.getDod(), formatter).atStartOfDay() : null);

        characterRequestValidation.validateCharacterRequestFields(new EntityRequest<>(characterEntity, request), BASIC_NOT_NULL);
        CharacterEntity createdCharacter = characterDAO.persist(characterEntity);

        if (requestEntity.getImageCreateRequest() != null && requestEntity.getImageCreateRequest().getImageData() != null
                && requestEntity.getImageCreateRequest().getImageName() != null) {
            CreateMediaRequest mediaRequest = new CreateMediaRequest(ObjectType.CHARACTER.getValue(), createdCharacter.getId(),
                    requestEntity.getImageCreateRequest().getImageData(), requestEntity.getImageCreateRequest().getImageName(), "IMAGE",
                    "COVER_IMAGE");

            mediaService.saveMedia(new EntityRequest<>(mediaRequest, request));
        }

        for (Long gameId : requestEntity.getGamesIds()) {
            LinkMapEntity linkMapEntity = new LinkMapEntity();
            final String uuid = UUID.randomUUID().toString();
            linkMapEntity.setUuid(uuid);
            linkMapEntity.setGame(gameDAO.findByPK(gameId));
            linkMapEntity.setCharacter(createdCharacter);
            linkMapEntity.setCreated(LocalDateTime.now());
            linkMapEntity.setCreatedBy(request.getUserId());
            linkMapDAO.merge(linkMapEntity);
        }

        CompleteCharacterResponse characterResponse = characterMapper.characterEntityToCompleteCharacter(characterEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, characterResponse);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<CompleteCharacterResponse> update(EntityRequest<CharacterUpdateRequest> request) throws ApiException {
        CharacterUpdateRequest requestEntity = request.getEntity();

        characterRequestValidation.validateCharacterExists(new EntityRequest<>(requestEntity.getId(), request), VALIDATE_ABSTRACT_REQUEST);

        final CharacterEntity characterEntity = characterDAO.findByPK(requestEntity.getId());
        characterMapper.updateDtoToEntity(requestEntity, characterEntity);

        characterRequestValidation.validateCharacterRequestFields(new EntityRequest<>(characterEntity, request), BASIC_NOT_NULL);

        characterEntity.setModified(LocalDateTime.now());
        characterEntity.setModifiedBy(request.getUserId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        characterEntity.setDob(requestEntity.getDob() != null ? LocalDate.parse(requestEntity.getDob(), formatter).atStartOfDay() : null);
        characterEntity.setDod(requestEntity.getDod() != null ? LocalDate.parse(requestEntity.getDod(), formatter).atStartOfDay() : null);

        if (requestEntity.getImageCreateRequest() != null && requestEntity.getImageCreateRequest().getImageData() != null
                && requestEntity.getImageCreateRequest().getImageName() != null) {
            CreateMediaRequest mediaRequest = new CreateMediaRequest(ObjectType.CHARACTER.getValue(), characterEntity.getId(),
                    requestEntity.getImageCreateRequest().getImageData(), requestEntity.getImageCreateRequest().getImageName(), "IMAGE",
                    "COVER_IMAGE");

            mediaService.saveMedia(new EntityRequest<>(mediaRequest, request));
        }

        characterDAO.removeAllGames(characterEntity.getId());
        for (Long gameId : requestEntity.getGamesIds()) {
            LinkMapEntity linkMapEntity = new LinkMapEntity();
            final String uuid = UUID.randomUUID().toString();
            linkMapEntity.setUuid(uuid);
            linkMapEntity.setGame(gameDAO.findByPK(gameId));
            linkMapEntity.setCharacter(characterEntity);
            linkMapEntity.setCreated(LocalDateTime.now());
            linkMapEntity.setCreatedBy(request.getUserId());
            linkMapDAO.merge(linkMapEntity);
        }

        characterDAO.merge(characterEntity);
        CompleteCharacterResponse characterResponse = characterMapper.characterEntityToCompleteCharacter(characterEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, characterResponse);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        characterRequestValidation.validateCharacterExists(entityRequest, VALIDATE_ABSTRACT_REQUEST);

        characterDAO.deleteRelations(request.getEntity());
        characterDAO.removeByPK(request.getEntity());

        return new PayloadResponse<>(request, ResponseCode.OK, "Character deleted!");
    }

    @Override
    public ListPayloadResponse<LoV> getLoVs(final ListRequest<Long> request) throws ApiException {
        if (request.getList() != null) {
            for (Long item : request.getList()) {
                EntityRequest<Long> longRequest = new EntityRequest<>(item, request);
                characterRequestValidation.validateCharacterExists(longRequest, VALIDATE_ABSTRACT_REQUEST);
            }
        }

        List<LoV> loVs = characterDAO.getLoVs(request.getList());

        return new ListPayloadResponse<>(request, ResponseCode.OK, loVs);
    }
}
