package ba.com.zira.praksa.core.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.ReleaseService;
import ba.com.zira.praksa.api.model.release.ReleaseRequest;
import ba.com.zira.praksa.api.model.release.ReleaseResponse;
import ba.com.zira.praksa.api.model.release.ReleasesByTimetableRequest;
import lombok.AllArgsConstructor;

/**
 * Validates findById, update, delete ReleaseResponse plan from
 * {@link ReleaseService}.
 *
 * @param request
 *            the {@link EntityRequest} to validate.
 * @param validationRuleMessage
 *            name of the validation rule that is going to be used for
 *            validating {@link ReleaseResponse}
 *
 * @return {@link ValidationResponse}
 */

@Component("releaseRequestValidation")
@AllArgsConstructor
public class ReleaseRequestValidation {

    private RequestValidator requestValidator;

    public ValidationResponse validateReleaseRequest(final EntityRequest<String> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (StringUtils.isBlank(request.getEntity())) {
                errorDescription.append(String.format("Release with uuid: %s does not exist!", request.getEntity()));

            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

    public ValidationResponse validateReleaseByTimetableRequest(final EntityRequest<ReleasesByTimetableRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (request.getEntity().getStartDate() == null || request.getEntity().getEndDate() == null
                    || request.getEntity().getTimeSegment() == null) {
                errorDescription.append("Start date, end date and time segment must be entered!");

            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

    public ValidationResponse validateDatesInRequest(final EntityRequest<ReleasesByTimetableRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!request.getEntity().getStartDate().isBefore(request.getEntity().getEndDate())) {
                errorDescription.append("Start date must be before end date!");

            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
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