package ba.com.zira.praksa.core.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.FeatureService;
import ba.com.zira.praksa.api.model.feature.FeatureCreateRequest;
import ba.com.zira.praksa.api.model.feature.FeatureUpdateRequest;
import ba.com.zira.praksa.dao.FeatureDAO;
import ba.com.zira.praksa.mapper.FeatureMapper;

/**
 * FeatureRequestValidation is used for validation of {@link FeatureService}
 * requests.
 *
 * @author zira
 *
 */
@Component("featureRequestValidation")
public class FeatureRequestValidation {

    RequestValidator requestValidator;
    FeatureDAO featureDAO;
    FeatureMapper featureMapper;

    public FeatureRequestValidation(RequestValidator requestValidator, FeatureDAO featureDAO, FeatureMapper featureMapper) {
        super();
        this.requestValidator = requestValidator;
        this.featureDAO = featureDAO;
        this.featureMapper = featureMapper;
    }

    /**
     * Validates if Feature exists in database.
     *
     * @param request
     *            the {@link EntityRequest} to validate.
     * @param validationRuleMessage
     *            name of the validation rule that is going to be used for
     *            validating Feature Id
     *
     * @return {@link ValidationResponse}
     */
    public ValidationResponse validateIfFeatureExists(final EntityRequest<Long> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);

        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!featureDAO.existsByPK(request.getEntity())) {
                errorDescription.append("Feature with id: ").append(request.getEntity()).append(" does not exist !");
            }

            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

    /**
     * Validates request(create) for Feature from {@link FeatureService}.
     *
     * @param request
     *            the {@link EntityRequest} to validate.
     * @param validationRuleMessage
     *            name of the validation rule that is going to be used for
     *            validating {@link FeatureCreateRequest}
     *
     * @return {@link ValidationResponse}
     */
    public ValidationResponse validateFeatureRequestFields(final EntityRequest<FeatureCreateRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);

        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();

            if (StringUtils.isBlank(request.getEntity().getName())) {
                errorDescription.append("Name is required.");
            }
            if (StringUtils.isBlank(request.getEntity().getType())) {
                errorDescription.append("Type is required.");
            }

            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

    /**
     * Validates request(update) for Feature from {@link FeatureService}. Checks
     * if Feature exists, if it does, then checks if data <br>
     * of sent a request is valid for update.
     *
     * @param request
     *            the {@link EntityRequest} to validate.
     *
     * @return {@link ValidationResponse}
     */
    public ValidationResponse validateBasicFeatureRequest(final EntityRequest<FeatureUpdateRequest> request) {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity().getId(), request);
        ValidationResponse validationResponseExists = validateIfFeatureExists(entityRequest, "validateAbstractRequest");

        if (StringUtils.isNotBlank(validationResponseExists.getDescription())) {
            return validationResponseExists;
        }

        ValidationResponse validationResponseFields = validateFeatureRequestFields(
                new EntityRequest<>(featureMapper.updateToCreateRequest(request.getEntity()), request), "basicNotNull");

        if (validationResponseFields.getResponseCode() == ResponseCode.OK.getCode()) {
            return validationResponseFields;
        }

        return validationResponseFields;
    }
}
