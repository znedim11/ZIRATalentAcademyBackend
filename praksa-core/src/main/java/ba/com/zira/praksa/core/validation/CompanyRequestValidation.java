package ba.com.zira.praksa.core.validation;

import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.model.company.CompanyUpdateRequest;
import ba.com.zira.praksa.dao.CompanyDAO;

@Component
public class CompanyRequestValidation {

    private RequestValidator requestValidator;
    private CompanyDAO companyDAO;

    public CompanyRequestValidation(final RequestValidator requestValidator, CompanyDAO companyDAO) {
        this.requestValidator = requestValidator;
        this.companyDAO = companyDAO;
    }

    public ValidationResponse validateUpdateFranchiseRequest(final EntityRequest<CompanyUpdateRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!companyDAO.existsByPK(request.getEntity().getId())) {
                errorDescription.append("Sample with id: ").append(request.getEntity().getId()).append(" does not exist !");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

    public ValidationResponse validateCompanyExists(final EntityRequest<Long> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);

        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!companyDAO.existsByPK(request.getEntity())) {
                errorDescription.append("Company with id: ").append(request.getEntity()).append(" does not exist !");
            }

            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }
}