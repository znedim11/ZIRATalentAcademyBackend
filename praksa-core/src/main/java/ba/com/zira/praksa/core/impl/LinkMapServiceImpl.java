package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
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
        linkMapRequestValidation.validateRequiredFieldsExist(request, "basicNotNull");
        linkMapRequestValidation.validateKeysExist(request, "validateAbstractRequest");

        LinkRequest requestEntity = request.getEntity();

        LinkMapEntity entity = new LinkMapEntity();
        entity.setUuid(UUID.randomUUID().toString());
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(request.getUserId());

        setLinkId(entity, requestEntity.getObjectAType(), requestEntity.getObjectAId());
        setLinkId(entity, requestEntity.getObjectBType(), requestEntity.getObjectBId());

        linkMapDAO.merge(entity);

        return new PayloadResponse<String>(request, ResponseCode.OK, "Objects linked!");
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
    public PayloadResponse<String> multiple(EntityRequest<MultipleLinkRequest> request) throws ApiException {
        // TODO Auto-generated method stub
        return null;
    }
}
