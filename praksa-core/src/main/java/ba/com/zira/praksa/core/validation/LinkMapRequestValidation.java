package ba.com.zira.praksa.core.validation;

import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.Filter;
import ba.com.zira.commons.model.FilterExpression;
import ba.com.zira.commons.model.FilterExpression.FilterOperation;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.api.model.linkmap.LinkRequest;
import ba.com.zira.praksa.api.model.linkmap.MultipleLinkRequest;
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
import ba.com.zira.praksa.dao.model.LinkMapEntity_;
import ba.com.zira.praksa.dao.model.LocationEntity;
import ba.com.zira.praksa.dao.model.ObjectEntity;
import ba.com.zira.praksa.dao.model.PersonEntity;
import lombok.AllArgsConstructor;

/**
 * @author zira
 *
 */

@Component("linkMapRequestValidation")
@AllArgsConstructor
public class LinkMapRequestValidation {
    LinkMapDAO linkMapDAO;
    RequestValidator requestValidator;
    CharacterDAO characterDAO;
    ConceptDAO conceptDAO;
    GameDAO gameDAO;
    LocationDAO locationDAO;
    ObjectDAO objectDAO;
    PersonDAO personDAO;

    public ValidationResponse validateEntityExistsInLinkRequest(final EntityRequest<?> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (request.getEntity() == null) {
                errorDescription.append("Entity must exist in request!");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }

    public ValidationResponse validateRequiredFieldsExistInSingleLinkRequest(final EntityRequest<LinkRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (StringUtils.isBlank(request.getEntity().getObjectAType())) {
                errorDescription.append("ObjectAType must exist!");
            }
            if (request.getEntity().getObjectAId() == null) {
                errorDescription.append("ObjectAId must exist!");
            }
            if (StringUtils.isBlank(request.getEntity().getObjectBType())) {
                errorDescription.append("ObjectBType must exist!");
            }
            if (request.getEntity().getObjectBId() == null) {
                errorDescription.append("ObjectBId must exist!");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }

    public ValidationResponse validateKeysExistInSingleLinkRequest(final EntityRequest<LinkRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        LinkRequest requestEntity = request.getEntity();

        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();

            checkKey(errorDescription, requestEntity.getObjectAType(), requestEntity.getObjectAId());
            checkKey(errorDescription, requestEntity.getObjectBType(), requestEntity.getObjectBId());

            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }

    public ValidationResponse validateLinkDoesNotExistInSingleLinkRequest(final EntityRequest<LinkRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        LinkRequest requestEntity = request.getEntity();

        Filter filter = new Filter();
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();

            createFilter(filter, requestEntity.getObjectAType(), requestEntity.getObjectAId());
            createFilter(filter, requestEntity.getObjectBType(), requestEntity.getObjectBId());

            if (!filter.getFilterExpressions().isEmpty() && !linkMapDAO.findAll(filter).getRecords().isEmpty()) {

                errorDescription.append(String.format("Objects %s#%d and %s#%d are already linked", requestEntity.getObjectAType(),
                        requestEntity.getObjectAId(), requestEntity.getObjectBType(), requestEntity.getObjectBId()));
            }

            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }

    public ValidationResponse validateRequiredFieldsExistInMultipleLinkRequest(final EntityRequest<MultipleLinkRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (StringUtils.isBlank(request.getEntity().getObjectAType())) {
                errorDescription.append("ObjectAType must exist!");
            }
            if (request.getEntity().getObjectAId() == null) {
                errorDescription.append("ObjectAId must exist!");
            }
            if (request.getEntity().getObjectBMap() == null || request.getEntity().getObjectBMap().size() == 0) {
                errorDescription.append("ObjectBMap must exist!");
            }

            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }

    public ValidationResponse validateKeysExistInMultipleLinkRequest(final EntityRequest<MultipleLinkRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        MultipleLinkRequest requestEntity = request.getEntity();

        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();

            checkKey(errorDescription, requestEntity.getObjectAType(), requestEntity.getObjectAId());

            Set<Entry<String, Long>> requestMap = request.getEntity().getObjectBMap().entrySet();
            for (Entry<String, Long> requestMapEntry : requestMap) {

                checkKey(errorDescription, requestMapEntry.getKey(), requestMapEntry.getValue());
            }

            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }

    public ValidationResponse validateLinkDoesNotExistInMultipleLinkRequest(final EntityRequest<MultipleLinkRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        MultipleLinkRequest requestEntity = request.getEntity();

        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();

            Set<Entry<String, Long>> requestMap = request.getEntity().getObjectBMap().entrySet();
            for (Entry<String, Long> requestMapEntry : requestMap) {
                Filter filter = new Filter();
                createFilter(filter, requestEntity.getObjectAType(), requestEntity.getObjectAId());
                createFilter(filter, requestMapEntry.getKey(), requestMapEntry.getValue());

                if (!filter.getFilterExpressions().isEmpty() && !linkMapDAO.findAll(filter).getRecords().isEmpty()) {

                    errorDescription.append(String.format("Objects %s#%d and %s#%d are already linked", requestEntity.getObjectAType(),
                            requestEntity.getObjectAId(), requestMapEntry.getKey(), requestMapEntry.getValue()));
                }
            }

            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }

    /**
     * Helper functions
     */

    private void checkKey(StringBuilder errorDescription, String objectType, Long objectId) {
        if (ObjectType.CHARACTER.getValue().equalsIgnoreCase(objectType)) {
            if (!characterDAO.existsByPK(objectId)) {
                errorDescription.append(String.format("Character with id %d does not exist!", objectId));
            }
        } else if (ObjectType.CONCEPT.getValue().equalsIgnoreCase(objectType)) {
            if (!conceptDAO.existsByPK(objectId)) {
                errorDescription.append(String.format("Concept with id %d does not exist!", objectId));
            }
        } else if (ObjectType.GAME.getValue().equalsIgnoreCase(objectType)) {
            if (!gameDAO.existsByPK(objectId)) {
                errorDescription.append(String.format("Game with id %d does not exist!", objectId));
            }
        } else if (ObjectType.LOCATION.getValue().equalsIgnoreCase(objectType)) {
            if (!locationDAO.existsByPK(objectId)) {
                errorDescription.append(String.format("Location with id %d does not exist!", objectId));
            }
        } else if (ObjectType.OBJECT.getValue().equalsIgnoreCase(objectType)) {
            if (!objectDAO.existsByPK(objectId)) {
                errorDescription.append(String.format("Object with id %d does not exist!", objectId));
            }
        } else if (ObjectType.PERSON.getValue().equalsIgnoreCase(objectType)) {
            if (!personDAO.existsByPK(objectId)) {
                errorDescription.append(String.format("Person with id %d does not exist!", objectId));
            }
        } else {
            errorDescription.append(String.format("Type %s does not exist!", objectType));
        }
    }

    private void createFilter(Filter filter, String objectType, Long objectId) {
        if (ObjectType.CHARACTER.getValue().equalsIgnoreCase(objectType)) {
            CharacterEntity entity = characterDAO.findByPK(objectId);
            filter.addFilterExpression(new FilterExpression(LinkMapEntity_.character.getName(), FilterOperation.EQUALS, entity));
        } else if (ObjectType.CONCEPT.getValue().equalsIgnoreCase(objectType)) {
            ConceptEntity entity = conceptDAO.findByPK(objectId);
            filter.addFilterExpression(new FilterExpression(LinkMapEntity_.concept.getName(), FilterOperation.EQUALS, entity));
        } else if (ObjectType.GAME.getValue().equalsIgnoreCase(objectType)) {
            GameEntity entity = gameDAO.findByPK(objectId);
            filter.addFilterExpression(new FilterExpression(LinkMapEntity_.game.getName(), FilterOperation.EQUALS, entity));
        } else if (ObjectType.LOCATION.getValue().equalsIgnoreCase(objectType)) {
            LocationEntity entity = locationDAO.findByPK(objectId);
            filter.addFilterExpression(new FilterExpression(LinkMapEntity_.location.getName(), FilterOperation.EQUALS, entity));
        } else if (ObjectType.OBJECT.getValue().equalsIgnoreCase(objectType)) {
            ObjectEntity entity = objectDAO.findByPK(objectId);
            filter.addFilterExpression(new FilterExpression(LinkMapEntity_.object.getName(), FilterOperation.EQUALS, entity));
        } else if (ObjectType.PERSON.getValue().equalsIgnoreCase(objectType)) {
            PersonEntity entity = personDAO.findByPK(objectId);
            filter.addFilterExpression(new FilterExpression(LinkMapEntity_.person.getName(), FilterOperation.EQUALS, entity));
        }
    }

}
