package ba.com.zira.praksa.core.validation;

import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.LocationService;
import ba.com.zira.praksa.api.model.location.Location;
import ba.com.zira.praksa.dao.LocationDAO;

/**
 * SampleRequestValidation is used for validation of {@link LocationService}
 * requests.<br>
 * e.g. database validation needed
 *
 * @author zira
 *
 */
@Component("locationRequestValidation")
public class LocationRequestValidation {

    private RequestValidator requestValidator;
    private LocationDAO locationDAO;

    public LocationRequestValidation(final RequestValidator requestValidator, LocationDAO locationDAO) {
        this.requestValidator = requestValidator;
        this.locationDAO = locationDAO;
    }

    /**
     * Validates update Location plan from {@link LocationService}.
     *
     * @param request
     *            the {@link EntityRequest} to validate.
     * @param validationRuleMessage
     *            name of the validation rule that is going to be used for
     *            validating {@link Location}
     *
     * @return {@link ValidationResponse}
     */
    public ValidationResponse validateUpdateLocationRequest(final EntityRequest<Location> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!locationDAO.existsByPK(request.getEntity().getId())) {
                errorDescription.append("Sample with id: ").append(request.getEntity().getId()).append(" does not exist !");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

    public ValidationResponse validateLocationExists(final EntityRequest<Long> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!locationDAO.existsByPK(request.getEntity())) {
                errorDescription.append("Location with id: ").append(request.getEntity()).append(" does not exist !");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

}
