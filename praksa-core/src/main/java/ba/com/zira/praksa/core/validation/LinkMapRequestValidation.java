package ba.com.zira.praksa.core.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.api.model.linkMap.LinkRequest;
import ba.com.zira.praksa.dao.CharacterDAO;
import ba.com.zira.praksa.dao.ConceptDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.LinkMapDAO;
import ba.com.zira.praksa.dao.LocationDAO;
import ba.com.zira.praksa.dao.ObjectDAO;
import ba.com.zira.praksa.dao.PersonDAO;
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
        if (ObjectType.CHARACTER.getValue().equalsIgnoreCase(objectType) && !characterDAO.existsByPK(objectId)) {
            errorDescription.append("Character with id ").append(objectId).append(" does not exist!");
        } else if (ObjectType.CONCEPT.getValue().equalsIgnoreCase(objectType) && !conceptDAO.existsByPK(objectId)) {
            errorDescription.append("Concept with id ").append(objectId).append(" does not exist!");
        } else if (ObjectType.GAME.getValue().equalsIgnoreCase(objectType) && !gameDAO.existsByPK(objectId)) {
            errorDescription.append("Game with id ").append(objectId).append(" does not exist!");
        } else if (ObjectType.LOCATION.getValue().equalsIgnoreCase(objectType) && !locationDAO.existsByPK(objectId)) {
            errorDescription.append("Location with id ").append(objectId).append(" does not exist!");
        } else if (ObjectType.OBJECT.getValue().equalsIgnoreCase(objectType) && !objectDAO.existsByPK(objectId)) {
            errorDescription.append("Object with id ").append(objectId).append(" does not exist!");
        } else if (ObjectType.PERSON.getValue().equalsIgnoreCase(objectType) && !personDAO.existsByPK(objectId)) {
            errorDescription.append("Person with id ").append(objectId).append(" does not exist!");
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

    public ValidationResponse validateLinkExist(final EntityRequest<LinkRequest> request, final String validationRuleMessage) {
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
}
