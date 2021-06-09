package ba.com.zira.praksa.core.validation;

import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.model.feature.Feature;
import ba.com.zira.praksa.dao.FeatureDAO;

/**
 * 
 * @author Ajas
 *
 */
@Component("featureRequestValidation")
public class FeatureRequestValidation {

    private RequestValidator requestValidator;
    private FeatureDAO featureDAO;

    public FeatureRequestValidation(final RequestValidator requestValidator, FeatureDAO featureDAO) {
        this.requestValidator = requestValidator;
        this.featureDAO = featureDAO;
    }

    public ValidationResponse validateFeatureUpdateRequest(final EntityRequest<Feature> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);

        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!featureDAO.existsByPK(request.getEntity().getId())) {
                errorDescription.append("Sample with id: ").append(request.getEntity().getId()).append(" does not exist !");
            }

            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }
}
