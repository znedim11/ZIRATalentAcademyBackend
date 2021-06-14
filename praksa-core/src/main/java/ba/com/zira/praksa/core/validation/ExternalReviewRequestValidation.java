package ba.com.zira.praksa.core.validation;

import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.model.externalReview.ExternalReviewCreateRequest;
import ba.com.zira.praksa.api.model.externalReview.ExternalReviewUpdateRequest;
import ba.com.zira.praksa.dao.ExternalReviewDAO;
import ba.com.zira.praksa.dao.RssFeedDAO;

@Component("externalReviewRequestValidation")
public class ExternalReviewRequestValidation {
    private RequestValidator requestValidator;
    private ExternalReviewDAO externalReviewDAO;
    private RssFeedDAO rssFeedDAO;

    public ExternalReviewRequestValidation(final RequestValidator requestValidator, ExternalReviewDAO externalReviewDAO,
            RssFeedDAO rssFeedDAO) {
        this.requestValidator = requestValidator;
        this.externalReviewDAO = externalReviewDAO;
        this.rssFeedDAO = rssFeedDAO;
    }

    public ValidationResponse validateExternalReviewExists(final EntityRequest<Long> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!externalReviewDAO.existsByPK(request.getEntity())) {
                errorDescription.append("External review with id ").append(request.getEntity()).append(" does not exist !");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

    public ValidationResponse validateEntityExistsInCreateRequest(final EntityRequest<ExternalReviewCreateRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (request.getEntity() == null) {
                errorDescription.append("Entity does not exist!");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

    public ValidationResponse validateEntityExistsInUpdateRequest(final EntityRequest<ExternalReviewUpdateRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (request.getEntity() == null) {
                errorDescription.append("Entity does not exist!");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

    public ValidationResponse validateRssFeedExists(final EntityRequest<Long> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!rssFeedDAO.existsByPK(request.getEntity())) {
                errorDescription.append("RssFeed with id ").append(request.getEntity()).append(" does not exist !");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

}
