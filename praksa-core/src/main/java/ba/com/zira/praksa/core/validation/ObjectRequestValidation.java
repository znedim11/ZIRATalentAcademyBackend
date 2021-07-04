package ba.com.zira.praksa.core.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.ObjectService;
import ba.com.zira.praksa.api.model.object.ObjectCreateRequest;
import ba.com.zira.praksa.api.model.object.ObjectUpdateRequest;
import ba.com.zira.praksa.dao.ObjectDAO;
import ba.com.zira.praksa.mapper.ObjectMapper;
import lombok.AllArgsConstructor;

/**
 * ObjectRequestValidation is used for validation of {@link ObjectService}
 * requests.
 *
 * @author zira
 *
 */
@AllArgsConstructor
@Component("objectRequestValidation")
public class ObjectRequestValidation {

    RequestValidator requestValidator;
    ObjectDAO objectDAO;
    ObjectMapper objectMapper;

    /**
     * Validates if Object exists in database.
     *
     * @param request
     *            the {@link EntityRequest} to validate.
     * @param validationRuleMessage
     *            name of the validation rule that is going to be used for
     *            validating {@link ObjectCreateRequest}
     *
     * @return {@link ValidationResponse}
     */
    public ValidationResponse validateObjectExists(final EntityRequest<Long> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);

        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!objectDAO.existsByPK(request.getEntity())) {
                errorDescription.append("Object ID  ").append(request.getEntity()).append(" does not exist !");
            }

            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

    /**
     * Validates request(create) for Object from {@link ObjectService}.
     *
     * @param request
     *            the {@link EntityRequest} to validate.
     * @param validationRuleMessage
     *            name of the validation rule that is going to be used for
     *            validating {@link ObjectCreateRequest}
     *
     * @return {@link ValidationResponse}
     */
    public ValidationResponse validateObjectRequestFields(final EntityRequest<ObjectCreateRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);

        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();

            if (StringUtils.isBlank(request.getEntity().getName())) {
                errorDescription.append("Object name is required.");
            }
            if (StringUtils.isBlank(request.getEntity().getInformation())) {
                errorDescription.append("Object information is required.");
            }

            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

    /**
     * Validates request(update) for Object from {@link ObjectService}. Checks
     * if Object exists, if it does, then checks if data <br>
     * of sent a request is valid for update.
     *
     * @param request
     *            the {@link EntityRequest} to validate.
     *
     * @return {@link ValidationResponse}
     */
    public ValidationResponse validateObjectUpdate(final EntityRequest<ObjectUpdateRequest> request) {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity().getId(), request);
        ValidationResponse validationResponseExists = validateObjectExists(entityRequest, "validateAbstractRequest");

        if (StringUtils.isNotBlank(validationResponseExists.getDescription())) {
            return validationResponseExists;
        }

        ValidationResponse validationResponseFields = validateObjectRequestFields(
                new EntityRequest<>(objectMapper.updateToCreateRequest(request.getEntity()), request), "basicNotNull");

        if (validationResponseFields.getResponseCode() == ResponseCode.OK.getCode()) {
            return validationResponseFields;
        }

        return validationResponseFields;
    }
}
