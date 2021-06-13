package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.LinkMapService;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.api.model.linkMap.LinkRequest;
import ba.com.zira.praksa.api.model.linkMap.MultipleLinkRequest;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LinkMapServiceImpl implements LinkMapService {
    LinkMapRequestValidation linkMapRequestValidation;

    LinkMapDAO linkMapDAO;
    CharacterDAO characterDAO;
    ConceptDAO conceptDAO;
    GameDAO gameDAO;
    LocationDAO locationDAO;
    ObjectDAO objectDAO;
    PersonDAO personDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayloadResponse<String> single(EntityRequest<LinkRequest> request) throws ApiException {
        linkMapRequestValidation.validateEntityExistsInLinkRequest(request, "basicNotNull");
        linkMapRequestValidation.validateRequiredFieldsExistInLinkRequest(request, "basicNotNull");
        linkMapRequestValidation.validateKeysExistInLinkRequest(request, "validateAbstractRequest");
        linkMapRequestValidation.validateLinkDoesNotExistInLinkRequest(request, "basicNotNull");

        LinkRequest requestEntity = request.getEntity();

        LinkMapEntity entity = createLinkMapEntity(requestEntity.getObjectAType(), requestEntity.getObjectAId(),
                requestEntity.getObjectBType(), requestEntity.getObjectBId(), request.getUserId());

        linkMapDAO.merge(entity);

        return new PayloadResponse<String>(request, ResponseCode.OK, "Objects linked!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayloadResponse<String> multiple(EntityRequest<MultipleLinkRequest> request) throws ApiException {
        linkMapRequestValidation.validateEntityExistsInMultipleLinkRequest(request, "basicNotNull");
        linkMapRequestValidation.validateRequiredFieldsExistInMultipleLinkRequest(request, "basicNotNull");
        linkMapRequestValidation.validateKeysExistInMultipleLinkRequest(request, "validateAbstractRequest");
        linkMapRequestValidation.validateLinkDoesNotExistInMultipleLinkRequest(request, "basicNotNull");

        Set<Entry<String, Long>> requestMap = request.getEntity().getObjectBMap().entrySet();
        for (Entry<String, Long> requestMapEntry : requestMap) {
            LinkMapEntity entity = createLinkMapEntity(request.getEntity().getObjectAType(), request.getEntity().getObjectAId(),
                    requestMapEntry.getKey(), requestMapEntry.getValue(), request.getUserId());

            linkMapDAO.merge(entity);
        }

        return new PayloadResponse<String>(request, ResponseCode.OK, "Objects linked!");
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

}
