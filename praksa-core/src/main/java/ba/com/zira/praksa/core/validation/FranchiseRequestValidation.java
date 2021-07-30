package ba.com.zira.praksa.core.validation;

import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.dao.FranchiseDAO;

@Component
public class FranchiseRequestValidation {

    private RequestValidator requestValidator;
    private FranchiseDAO franchiseDAO;

    public FranchiseRequestValidation(final RequestValidator requestValidator, FranchiseDAO franchiseDAO) {
        this.requestValidator = requestValidator;
        this.franchiseDAO = franchiseDAO;
    }

    public ValidationResponse validateUpdateFranchiseRequest(final EntityRequest<Long> request) {
        ValidationResponse validationResponse;
        StringBuilder errorDescription = new StringBuilder();
        if (!franchiseDAO.existsByPK(request.getEntity())) {
            errorDescription.append("Franchise with id: ").append(request.getEntity()).append(" does not exist !");
        }
        validationResponse = requestValidator.createResponse(request, errorDescription);
        return validationResponse;
    }

    public ValidationResponse validateFranchiseExists(final EntityRequest<Long> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!franchiseDAO.existsByPK(request.getEntity())) {
                errorDescription.append("Franchise with id ").append(request.getEntity()).append(" does not exist !");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }

}