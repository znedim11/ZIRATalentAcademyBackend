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
import ba.com.zira.praksa.api.model.linkMap.LinkRequest;
import ba.com.zira.praksa.api.model.linkMap.MultipleLinkRequest;
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
    private LinkMapDAO linkMapDAO;
    private RequestValidator requestValidator;

    CharacterDAO characterDAO;
    ConceptDAO conceptDAO;
    GameDAO gameDAO;
    LocationDAO locationDAO;
    ObjectDAO objectDAO;
    PersonDAO personDAO;

    public ValidationResponse validateKeysExist(final EntityRequest<LinkRequest> request, final String validationRuleMessage) {
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

    private void checkKey(StringBuilder errorDescription, String objectType, Long objectId) {
        if (ObjectType.CHARACTER.getValue().equalsIgnoreCase(objectType)) {
            if (!characterDAO.existsByPK(objectId)) {
                errorDescription.append("Character with id ").append(objectId).append(" does not exist!");
            }
        } else if (ObjectType.CONCEPT.getValue().equalsIgnoreCase(objectType)) {
            if (!conceptDAO.existsByPK(objectId)) {
                errorDescription.append("Concept with id ").append(objectId).append(" does not exist!");
            }
        } else if (ObjectType.GAME.getValue().equalsIgnoreCase(objectType)) {
            if (!gameDAO.existsByPK(objectId)) {
                errorDescription.append("Game with id ").append(objectId).append(" does not exist!");
            }
        } else if (ObjectType.LOCATION.getValue().equalsIgnoreCase(objectType)) {
            if (!locationDAO.existsByPK(objectId)) {
                errorDescription.append("Location with id ").append(objectId).append(" does not exist!");
            }
        } else if (ObjectType.OBJECT.getValue().equalsIgnoreCase(objectType)) {
            if (!objectDAO.existsByPK(objectId)) {
                errorDescription.append("Object with id ").append(objectId).append(" does not exist!");
            }
        } else if (ObjectType.PERSON.getValue().equalsIgnoreCase(objectType)) {
            if (!personDAO.existsByPK(objectId)) {
                errorDescription.append("Person with id ").append(objectId).append(" does not exist!");
            }
        } else {
            errorDescription.append("Type ").append(objectType).append(" does not exist!");
        }
    }

    public ValidationResponse validateRequiredFieldsExist(final EntityRequest<LinkRequest> request, final String validationRuleMessage) {
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

    public ValidationResponse validateEntityExistsInLinkRequest(final EntityRequest<LinkRequest> request,
            final String validationRuleMessage) {
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

    public ValidationResponse validateLinkDoesNotExist(final EntityRequest<LinkRequest> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        LinkRequest requestEntity = request.getEntity();

        Filter filter = new Filter();
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();

            createFilter(filter, requestEntity.getObjectAType(), requestEntity.getObjectAId());
            createFilter(filter, requestEntity.getObjectBType(), requestEntity.getObjectBId());

            if (filter.getFilterExpressions().size() > 0 && linkMapDAO.findAll(filter).getRecords().size() > 0) {
                errorDescription.append("Objects are already linked!");
            }

            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }

    private void createFilter(Filter filter, String objectType, Long objectId) {
        FilterExpression filterExpression = new FilterExpression();
        filterExpression.setFilterOperation(FilterOperation.EQUALS);

        if (ObjectType.CHARACTER.getValue().equalsIgnoreCase(objectType)) {
            filterExpression.setAttribute("character");

            CharacterEntity entity = characterDAO.findByPK(objectId);
            filterExpression.setExpressionValueObject(entity);

            filter.addFilterExpression(filterExpression);
        } else if (ObjectType.CONCEPT.getValue().equalsIgnoreCase(objectType)) {
            filterExpression.setAttribute("concept");

            ConceptEntity entity = conceptDAO.findByPK(objectId);
            filterExpression.setExpressionValueObject(entity);

            filter.addFilterExpression(filterExpression);
        } else if (ObjectType.GAME.getValue().equalsIgnoreCase(objectType)) {
            filterExpression.setAttribute("game");

            GameEntity entity = gameDAO.findByPK(objectId);
            filterExpression.setExpressionValueObject(entity);

            filter.addFilterExpression(filterExpression);
        } else if (ObjectType.LOCATION.getValue().equalsIgnoreCase(objectType)) {
            filterExpression.setAttribute("location");

            LocationEntity entity = locationDAO.findByPK(objectId);
            filterExpression.setExpressionValueObject(entity);

            filter.addFilterExpression(filterExpression);
        } else if (ObjectType.OBJECT.getValue().equalsIgnoreCase(objectType)) {
            filterExpression.setAttribute("object");

            ObjectEntity entity = objectDAO.findByPK(objectId);
            filterExpression.setExpressionValueObject(entity);

            filter.addFilterExpression(filterExpression);
        } else if (ObjectType.PERSON.getValue().equalsIgnoreCase(objectType)) {
            filterExpression.setAttribute("person");

            PersonEntity entity = personDAO.findByPK(objectId);
            filterExpression.setExpressionValueObject(entity);

            filter.addFilterExpression(filterExpression);
        }
    }

    public ValidationResponse validateMultipleKeysExist(final EntityRequest<MultipleLinkRequest> request,
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

    public ValidationResponse validateMultipleRequiredFieldsExist(final EntityRequest<MultipleLinkRequest> request,
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

    public ValidationResponse validateEntityExistsInMultipleLinkRequest(final EntityRequest<MultipleLinkRequest> request,
            final String validationRuleMessage) {
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

    public ValidationResponse validateMultipleLinkDoesNotExist(final EntityRequest<MultipleLinkRequest> request,
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

                if (filter.getFilterExpressions().size() > 0 && linkMapDAO.findAll(filter).getRecords().size() > 0) {
                    errorDescription.append("Objects ").append(requestEntity.getObjectAType()).append("#")
                            .append(requestEntity.getObjectAId()).append(" and ").append(requestMapEntry.getKey()).append("#")
                            .append(requestMapEntry.getValue()).append(" are already linked!");
                }
            }

            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }
}
