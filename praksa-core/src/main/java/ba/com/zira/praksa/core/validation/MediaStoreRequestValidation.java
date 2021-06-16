package ba.com.zira.praksa.core.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.MediaStoreService;
import ba.com.zira.praksa.api.model.mediastore.MediaStoreCreateRequest;
import ba.com.zira.praksa.api.model.mediastore.MediaStoreUpdateRequest;
import ba.com.zira.praksa.dao.MediaDAO;
import ba.com.zira.praksa.dao.MediaStoreDAO;
import lombok.AllArgsConstructor;

/**
 * SampleRequestValidation is used for validation of {@link MediaStoreService}
 * requests.<br>
 * e.g. database validation needed
 *
 * @author zira
 *
 */
@Component("componentRequestValidation")
@AllArgsConstructor
public class MediaStoreRequestValidation {
    private RequestValidator requestValidator;
    private MediaStoreDAO mediaStoreDAO;
    private MediaDAO mediaDAO;

    public ValidationResponse validateMediaStoreExists(final EntityRequest<String> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!mediaStoreDAO.existsByPK(request.getEntity())) {
                errorDescription.append("MediaStore with id ").append(request.getEntity()).append(" does not exist !");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }

    public ValidationResponse validateMediaStoreNameExists(final EntityRequest<String> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (StringUtils.isBlank(request.getEntity())) {
                errorDescription.append("MediaStore must have name!");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }

    public ValidationResponse validateEntityExistsInCreateRequest(final EntityRequest<MediaStoreCreateRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (request.getEntity() == null) {
                errorDescription.append("Entity must exist in request!");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }

    public ValidationResponse validateEntityExistsInUpdateRequest(final EntityRequest<MediaStoreUpdateRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (request.getEntity() == null) {
                errorDescription.append("Entity must exist in request!");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }

    public ValidationResponse validateMediaIdExists(final EntityRequest<Long> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!mediaDAO.existsByPK(request.getEntity())) {
                errorDescription.append("Media with id ").append(request.getEntity()).append(" does not exist !");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }
}
