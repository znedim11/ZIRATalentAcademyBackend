package ba.com.zira.praksa.core.validation;

import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.model.release.ReleaseRequest;

@Component("releaseRequestValidation")
public class ReleaseRequestValidation {

    private RequestValidator requestValidator;

    public ReleaseRequestValidation(final RequestValidator requestValidator) {
        this.requestValidator = requestValidator;
    }

    public ValidationResponse validateUpdateReleaseRequest(final EntityRequest<ReleaseRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

}