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
import ba.com.zira.praksa.api.model.concept.ConceptResponse;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.api.model.game.GameCharacterResponse;
import ba.com.zira.praksa.api.model.location.Location;
import ba.com.zira.praksa.api.model.media.CreateMediaRequest;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.api.model.person.Person;
import ba.com.zira.praksa.core.utils.LookupService;
import ba.com.zira.praksa.core.validation.CharacterRequestValidation;
import ba.com.zira.praksa.dao.CharacterDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.LinkMapDAO;
import ba.com.zira.praksa.dao.MediaDAO;
import ba.com.zira.praksa.dao.MediaStoreDAO;
import ba.com.zira.praksa.dao.model.CharacterEntity;
import ba.com.zira.praksa.dao.model.ConceptEntity;
import ba.com.zira.praksa.dao.model.ConceptEntity_;
import ba.com.zira.praksa.dao.model.GameEntity_;
import ba.com.zira.praksa.dao.model.LinkMapEntity;
import ba.com.zira.praksa.dao.model.LinkMapEntity_;
import ba.com.zira.praksa.dao.model.LocationEntity;
import ba.com.zira.praksa.dao.model.LocationEntity_;
import ba.com.zira.praksa.dao.model.MediaStoreEntity;
import ba.com.zira.praksa.dao.model.ObjectEntity;
import ba.com.zira.praksa.dao.model.ObjectEntity_;
import ba.com.zira.praksa.dao.model.PersonEntity;
import ba.com.zira.praksa.dao.model.PersonEntity_;
import ba.com.zira.praksa.mapper.CharacterMapper;
import ba.com.zira.praksa.mapper.ConceptMapper;
import ba.com.zira.praksa.mapper.LocationMapper;
import ba.com.zira.praksa.mapper.ObjectMapper;
import ba.com.zira.praksa.mapper.PersonMapper;

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
    MediaStoreDAO mediaStoreDAO;
    MediaDAO mediaDAO;
    CharacterMapper characterMapper;
    ConceptMapper conceptMapper;
    PersonMapper personMapper;
    ObjectMapper objectMapper;
    LocationMapper locationMapper;
    MediaService mediaService;
    LookupService lookupService;

    public CharacterServiceImpl(RequestValidator requestValidator, CharacterRequestValidation characterRequestValidation,
            CharacterDAO characterDAO, GameDAO gameDAO, CharacterMapper characterMapper, ConceptMapper conceptMapper,
            PersonMapper personMapper, ObjectMapper objectMapper, LocationMapper locationMapper, MediaService mediaService,
            LinkMapDAO linkMapDAO, MediaStoreDAO mediaStoreDAO, MediaDAO mediaDAO, LookupService lookupService) {
        super();
        this.requestValidator = requestValidator;
        this.characterRequestValidation = characterRequestValidation;
        this.characterDAO = characterDAO;
        this.gameDAO = gameDAO;
        this.linkMapDAO = linkMapDAO;
        this.mediaStoreDAO = mediaStoreDAO;
        this.mediaDAO = mediaDAO;
        this.characterMapper = characterMapper;
        this.conceptMapper = conceptMapper;
        this.personMapper = personMapper;
        this.objectMapper = objectMapper;
        this.locationMapper = locationMapper;
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
                CharacterSearchResponse::setImageUrl, CharacterSearchResponse::getImageUrl);

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
                CompleteCharacterResponse::setImageUrl, CompleteCharacterResponse::getImageUrl);

        return new PayloadResponse<>(request, ResponseCode.OK, temp.get(0));
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

        MediaStoreEntity mse = characterDAO.getCoverImage(request.getEntity().getId());

        if (mse != null) {
            Long mediaId = mse.getMedia().getId();
            mediaStoreDAO.remove(mse);
            mediaDAO.removeByPK(mediaId);
        }

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

    @Override
    public ListPayloadResponse<GameCharacterResponse> getGamesForCharacter(final EntityRequest<Long> request) throws ApiException {
        characterRequestValidation.validateCharacterExists(new EntityRequest<>(request.getEntity(), request), VALIDATE_ABSTRACT_REQUEST);

        List<GameCharacterResponse> gamesList = gameDAO.getGamesForCharacter(request.getEntity());

        return new ListPayloadResponse<>(request, ResponseCode.OK, gamesList);
    }

    @Override
    public ListPayloadResponse<ConceptResponse> getConceptsByCharacter(EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        characterRequestValidation.validateCharacterExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        List<ConceptEntity> entityList = characterDAO.getConceptsByCharacter(request.getEntity());
        List<ConceptResponse> locationList = conceptMapper.entityListToResponseList(entityList);

        return new ListPayloadResponse<>(request, ResponseCode.OK, locationList);
    }

    @Override
    public ListPayloadResponse<Location> getLocationsByCharacter(EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        characterRequestValidation.validateCharacterExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        List<LocationEntity> entityList = characterDAO.getLocationsByCharacter(request.getEntity());
        List<Location> locationList = locationMapper.entityListToDtoList(entityList);

        return new ListPayloadResponse<>(request, ResponseCode.OK, locationList);
    }

    @Override
    public ListPayloadResponse<ObjectResponse> getObjectsByCharacter(EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        characterRequestValidation.validateCharacterExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        List<ObjectEntity> entityList = characterDAO.getObjectsByCharacter(request.getEntity());
        List<ObjectResponse> objectList = objectMapper.entityListToDtoList(entityList);

        return new ListPayloadResponse<>(request, ResponseCode.OK, objectList);
    }

    @Override
    public ListPayloadResponse<Person> getPersonsByCharacter(final EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> longRequest = new EntityRequest<>(request.getEntity(), request);
        characterRequestValidation.validateCharacterExists(longRequest, VALIDATE_ABSTRACT_REQUEST);

        List<PersonEntity> entityList = characterDAO.getPersonsByCharacter(request.getEntity());
        List<Person> personList = personMapper.entityListToDtoList(entityList);

        return new ListPayloadResponse<>(request, ResponseCode.OK, personList);
    }

    @Override
    public ListPayloadResponse<LoV> getLoVsNotConnectedTo(final EntityRequest<LoV> request) throws ApiException {
        requestValidator.validate(request);

        String field = null;
        String idField = null;
        Long id = request.getEntity().getId();

        if (ObjectType.CONCEPT.getValue().equalsIgnoreCase(request.getEntity().getName())) {
            field = LinkMapEntity_.concept.getName();
            idField = ConceptEntity_.id.getName();
        } else if (ObjectType.GAME.getValue().equalsIgnoreCase(request.getEntity().getName())) {
            field = LinkMapEntity_.game.getName();
            idField = GameEntity_.id.getName();
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

        List<LoV> loVs = null;
        if (field != null) {
            loVs = characterDAO.getLoVsNotConnectedTo(field, idField, id);
        }

        return new ListPayloadResponse<>(request, ResponseCode.OK, loVs);
    }
}
