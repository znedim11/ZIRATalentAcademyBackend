package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
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
import ba.com.zira.praksa.api.LinkMapService;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.api.model.linkmap.LinkMapResponse;
import ba.com.zira.praksa.api.model.linkmap.LinkRequest;
import ba.com.zira.praksa.api.model.linkmap.MultipleLinkRequest;
import ba.com.zira.praksa.core.validation.LinkMapRequestValidation;
import ba.com.zira.praksa.dao.CharacterDAO;
import ba.com.zira.praksa.dao.ConceptDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.LinkMapDAO;
import ba.com.zira.praksa.dao.LocationDAO;
import ba.com.zira.praksa.dao.ObjectDAO;
import ba.com.zira.praksa.dao.PersonDAO;
import ba.com.zira.praksa.dao.model.CharacterEntity;
import ba.com.zira.praksa.dao.model.ConceptEntity;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.dao.model.LinkMapEntity;
import ba.com.zira.praksa.dao.model.LocationEntity;
import ba.com.zira.praksa.dao.model.ObjectEntity;
import ba.com.zira.praksa.dao.model.PersonEntity;
import ba.com.zira.praksa.mapper.LinkMapMapper;

@Service
public class LinkMapServiceImpl implements LinkMapService {
    static final String BASIC_NOT_NULL = "basicNotNull";

    RequestValidator requestValidator;
    LinkMapRequestValidation linkMapRequestValidation;
    LinkMapDAO linkMapDAO;
    CharacterDAO characterDAO;
    ConceptDAO conceptDAO;
    GameDAO gameDAO;
    LocationDAO locationDAO;
    ObjectDAO objectDAO;
    PersonDAO personDAO;
    LinkMapMapper linkMapMapper;

    public LinkMapServiceImpl(LinkMapRequestValidation linkMapRequestValidation, LinkMapDAO linkMapDAO, CharacterDAO characterDAO,
            ConceptDAO conceptDAO, GameDAO gameDAO, LocationDAO locationDAO, ObjectDAO objectDAO, PersonDAO personDAO) {
        super();
        this.linkMapRequestValidation = linkMapRequestValidation;
        this.linkMapDAO = linkMapDAO;
        this.characterDAO = characterDAO;
        this.conceptDAO = conceptDAO;
        this.gameDAO = gameDAO;
        this.locationDAO = locationDAO;
        this.objectDAO = objectDAO;
        this.personDAO = personDAO;
    }

    @Override
    public PagedPayloadResponse<LinkMapResponse> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<LinkMapEntity> linkMapEntitesData = linkMapDAO.findAll(request.getFilter());
        List<LinkMapEntity> linkMapEntites = linkMapEntitesData.getRecords();

        final List<LinkMapResponse> linkMapList = linkMapMapper.entityListToResponseList(linkMapEntites);

        PagedData<LinkMapResponse> pagedData = new PagedData<LinkMapResponse>();
        pagedData.setNumberOfPages(linkMapEntitesData.getNumberOfPages());
        pagedData.setNumberOfRecords(linkMapEntitesData.getNumberOfRecords());
        pagedData.setPage(linkMapEntitesData.getPage());
        pagedData.setRecords(linkMapList);
        pagedData.setRecordsPerPage(linkMapEntitesData.getRecordsPerPage());

        return new PagedPayloadResponse<LinkMapResponse>(request, ResponseCode.OK, pagedData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayloadResponse<String> createSingleLinkRequest(EntityRequest<LinkRequest> request) throws ApiException {
        linkMapRequestValidation.validateEntityExistsInLinkRequest(request, BASIC_NOT_NULL);
        linkMapRequestValidation.validateRequiredFieldsExistInSingleLinkRequest(request, BASIC_NOT_NULL);
        linkMapRequestValidation.validateKeysExistInSingleLinkRequest(request, "validateAbstractRequest");
        linkMapRequestValidation.validateLinkDoesNotExistInSingleLinkRequest(request, BASIC_NOT_NULL);

        LinkRequest requestEntity = request.getEntity();

        LinkMapEntity entity = createLinkMapEntity(requestEntity.getObjectAType(), requestEntity.getObjectAId(),
                requestEntity.getObjectBType(), requestEntity.getObjectBId(), request.getUserId());

        linkMapDAO.merge(entity);

        return new PayloadResponse<>(request, ResponseCode.OK, "Objects linked!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayloadResponse<String> createMultipleLinkRequest(EntityRequest<MultipleLinkRequest> request) throws ApiException {
        linkMapRequestValidation.validateEntityExistsInLinkRequest(request, BASIC_NOT_NULL);
        linkMapRequestValidation.validateRequiredFieldsExistInMultipleLinkRequest(request, BASIC_NOT_NULL);
        linkMapRequestValidation.validateKeysExistInMultipleLinkRequest(request, "validateAbstractRequest");
        linkMapRequestValidation.validateLinkDoesNotExistInMultipleLinkRequest(request, BASIC_NOT_NULL);

        Set<Entry<String, Long>> requestMap = request.getEntity().getObjectBMap().entrySet();
        for (Entry<String, Long> requestMapEntry : requestMap) {
            LinkMapEntity entity = createLinkMapEntity(request.getEntity().getObjectAType(), request.getEntity().getObjectAId(),
                    requestMapEntry.getKey(), requestMapEntry.getValue(), request.getUserId());

            linkMapDAO.merge(entity);
        }

        return new PayloadResponse<>(request, ResponseCode.OK, "Objects linked!");
    }

    /**
     * Helper functions
     */

    private LinkMapEntity createLinkMapEntity(String objectAType, Long objectAId, String objectBType, Long objectBId, String userId) {
        LinkMapEntity entity = new LinkMapEntity();
        entity.setUuid(UUID.randomUUID().toString());
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(userId);

        setLinkId(entity, objectAType, objectAId);
        setLinkId(entity, objectBType, objectBId);

        return entity;
    }

    private void setLinkId(LinkMapEntity entity, String objectType, Long objectId) {
        if (ObjectType.CHARACTER.getValue().equalsIgnoreCase(objectType)) {
            CharacterEntity objEntity = characterDAO.findByPK(objectId);
            entity.setCharacter(objEntity);
        } else if (ObjectType.CONCEPT.getValue().equalsIgnoreCase(objectType)) {
            ConceptEntity objEntity = conceptDAO.findByPK(objectId);
            entity.setConcept(objEntity);
        } else if (ObjectType.GAME.getValue().equalsIgnoreCase(objectType)) {
            GameEntity objEntity = gameDAO.findByPK(objectId);
            entity.setGame(objEntity);
        } else if (ObjectType.LOCATION.getValue().equalsIgnoreCase(objectType)) {
            LocationEntity objEntity = locationDAO.findByPK(objectId);
            entity.setLocation(objEntity);
        } else if (ObjectType.OBJECT.getValue().equalsIgnoreCase(objectType)) {
            ObjectEntity objEntity = objectDAO.findByPK(objectId);
            entity.setObject(objEntity);
        } else if (ObjectType.PERSON.getValue().equalsIgnoreCase(objectType)) {
            PersonEntity objEntity = personDAO.findByPK(objectId);
            entity.setPerson(objEntity);
        }
    }

    @Override
    public PayloadResponse<LinkMapResponse> findByUuid(EntityRequest<String> request) throws ApiException {
        linkMapRequestValidation.validateLinkMapExists(request, "validateAbstractRequest");

        final LinkMapEntity linkMapEntity = linkMapDAO.findByPK(request.getEntity());

        final LinkMapResponse linkMapResponse = linkMapMapper.entityToDto(linkMapEntity);

        return new PayloadResponse<LinkMapResponse>(request, ResponseCode.OK, linkMapResponse);
    }

    private LinkMapEntity updateLinkMapEntity(String objectAType, Long objectAId, String objectBType, Long objectBId, String userId) {
        LinkMapEntity entity = new LinkMapEntity();
        entity.setModified(LocalDateTime.now());
        entity.setModifiedBy(userId);

        setLinkId(entity, objectAType, objectAId);
        setLinkId(entity, objectBType, objectBId);

        return entity;
    }

    @Override
    public PayloadResponse<String> delete(EntityRequest<String> request) throws ApiException {
        EntityRequest<String> entityRequest = new EntityRequest<String>(request.getEntity(), request);
        linkMapRequestValidation.validateLinkMapExists(entityRequest, "validateAbstractRequest");

        linkMapDAO.removeByPK(request.getEntity());

        return new PayloadResponse<String>(request, ResponseCode.OK, "Concept deleted!");
    }

}
