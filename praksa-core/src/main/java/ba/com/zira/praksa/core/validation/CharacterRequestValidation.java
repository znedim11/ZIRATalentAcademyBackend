/**
 *
 */
package ba.com.zira.praksa.core.validation;

import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.dao.CharacterDAO;

/**
 * @author zira
 *
 */

@Component("characterRequestValidation")
public class CharacterRequestValidation {
    RequestValidator requestValidator;
    CharacterDAO characterDAO;

    public CharacterRequestValidation(RequestValidator requestValidator, CharacterDAO characterDAO) {
        super();
        this.requestValidator = requestValidator;
        this.characterDAO = characterDAO;
    }

    public ValidationResponse validateCharacterExists(final EntityRequest<Long> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!characterDAO.existsByPK(request.getEntity())) {
                errorDescription.append("Character with id ").append(request.getEntity()).append(" does not exist !");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }
}
