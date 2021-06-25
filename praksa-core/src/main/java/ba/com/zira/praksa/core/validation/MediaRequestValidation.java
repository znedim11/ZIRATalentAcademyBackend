package ba.com.zira.praksa.core.validation;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.model.media.Media;
import ba.com.zira.praksa.dao.MediaDAO;

public class MediaRequestValidation {

    private RequestValidator requestValidator;
    private MediaDAO mediaDAO;

    public MediaRequestValidation(final RequestValidator requestValidator, MediaDAO mediaDAO) {
        this.requestValidator = requestValidator;
        this.mediaDAO = mediaDAO;
    }

    public ValidationResponse validateUpdateGameRequest(final EntityRequest<Media> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!mediaDAO.existsByPK(request.getEntity().getId())) {
                errorDescription.append("Sample with id: ").append(request.getEntity().getId()).append(" does not exist !");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

}
