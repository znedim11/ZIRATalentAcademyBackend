/**
 *
 */
package ba.com.zira.praksa.core.validation;

import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.dao.ConceptDAO;

/**
 * @author zira
 *
 */

@Component("componentRequestValidation")
public class ConceptRequestValidation {
    private RequestValidator requestValidator;
    private ConceptDAO conceptDAO;

    public ConceptRequestValidation(final RequestValidator requestValidator, ConceptDAO conceptDAO) {
        this.requestValidator = requestValidator;
        this.conceptDAO = conceptDAO;
    }

    public ValidationResponse validateConceptExists(final EntityRequest<Long> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!conceptDAO.existsByPK(request.getEntity())) {
                errorDescription.append("Concept with id ").append(request.getEntity()).append(" does not exist !");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }
}
