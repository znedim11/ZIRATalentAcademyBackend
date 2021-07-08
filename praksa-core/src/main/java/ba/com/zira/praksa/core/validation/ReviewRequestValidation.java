/**
 *
 */
package ba.com.zira.praksa.core.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.model.review.ReviewCreateRequest;
import ba.com.zira.praksa.api.model.review.ReviewUpdateRequest;
import ba.com.zira.praksa.dao.ReviewDAO;

/**
 * @author zira
 *
 */

@Component("reviewRequestValidation")
public class ReviewRequestValidation {
    RequestValidator requestValidator;
    ReviewDAO reviewDAO;

    public ReviewRequestValidation(RequestValidator requestValidator, ReviewDAO reviewDAO) {
        super();
        this.requestValidator = requestValidator;
        this.reviewDAO = reviewDAO;
    }

    public ValidationResponse validateReviewExists(final EntityRequest<Long> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!reviewDAO.existsByPK(request.getEntity())) {
                errorDescription.append("Review with id ").append(request.getEntity()).append(" does not exist !");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }

    public ValidationResponse validateRequiredFieldsExistsInCreateRequest(final EntityRequest<ReviewCreateRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            ReviewCreateRequest entity = request.getEntity();
            StringBuilder errorDescription = new StringBuilder();
            if (StringUtils.isBlank(entity.getTitle())) {
                errorDescription.append("Review must have title! ");
            }
            if (entity.getGameId() == null) {
                errorDescription.append("Review must have gameId! ");
            }
            if (entity.getFormulaId() == null) {
                errorDescription.append("Review must have formulaId! ");
            }
            if (entity.getGrades() == null) {
                errorDescription.append("Review must have grades! ");
            }
            if (entity.getTotalRating() == null) {
                errorDescription.append("Review must have total rating! ");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }

    public ValidationResponse validateRequiredFieldsExistsInUpdateRequest(final EntityRequest<ReviewUpdateRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            ReviewUpdateRequest entity = request.getEntity();
            StringBuilder errorDescription = new StringBuilder();
            if (StringUtils.isBlank(entity.getTitle())) {
                errorDescription.append("Review must have title! ");
            }
            if (entity.getFormulaId() == null) {
                errorDescription.append("Review must have formulaId! ");
            }
            if (entity.getGrades() == null) {
                errorDescription.append("Review must have grades! ");
            }
            if (entity.getTotalRating() == null) {
                errorDescription.append("Review must have total rating! ");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }

    public ValidationResponse validateEntityExists(final EntityRequest<?> request, final String validationRuleMessage) {
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

}
