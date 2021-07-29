package ba.com.zira.praksa.core.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.model.enums.ReleaseType;
import ba.com.zira.praksa.api.model.release.ReleaseRequest;
import ba.com.zira.praksa.api.model.release.ReleasesByTimetableRequest;

@Component("releaseRequestValidation")
public class ReleaseRequestValidation {

    private RequestValidator requestValidator;
    private RegionRequestValidation regionRequestValidation;
    private GameRequestValidation gameRequestValidation;
    private PlatformRequestValidation platformRequestValidation;
    private CompanyRequestValidation companyRequestValidation;

    public ReleaseRequestValidation(RequestValidator requestValidator, RegionRequestValidation requestValidation,
            GameRequestValidation gameRequestValidation, PlatformRequestValidation platformRequestValidation,
            CompanyRequestValidation companyRequestValidation) {
        super();
        this.requestValidator = requestValidator;
        this.regionRequestValidation = requestValidation;
        this.gameRequestValidation = gameRequestValidation;
        this.platformRequestValidation = platformRequestValidation;
        this.companyRequestValidation = companyRequestValidation;
    }

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

    public ValidationResponse validateEntityExistsInRequest(final EntityRequest<?> request, final String validationRuleMessage) {
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

    public ValidationResponse validateRequiredFields(final EntityRequest<ReleaseRequest> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (request.getEntity().getReleaseDate() == null) {
                errorDescription.append("Release must have date!");
            }

            if (request.getEntity().getType() == null || StringUtils.isBlank(request.getEntity().getType())) {
                errorDescription.append("Release must have type!");
                return requestValidator.createResponse(request, errorDescription);
            } else if (!request.getEntity().getType().equals(ReleaseType.GAME.getValue())
                    && !request.getEntity().getType().equals(ReleaseType.PLATFORM.getValue())) {
                errorDescription.append("Unknown release type!");
                return requestValidator.createResponse(request, errorDescription);
            }

            if (request.getEntity().getRegionId() == null) {
                errorDescription.append("Release must have region!");
                return requestValidator.createResponse(request, errorDescription);
            } else {
                EntityRequest<Long> regionRequest = new EntityRequest<>(request.getEntity().getRegionId(), request);
                regionRequestValidation.validateRegionExists(regionRequest, validationRuleMessage);

            }

            if (request.getEntity().getPlatformId() == null && request.getEntity().getGameId() == null) {
                errorDescription.append("Release must be related to platform or game!");
            } else if (request.getEntity().getPlatformId() == null
                    && request.getEntity().getType().equals(ReleaseType.PLATFORM.getValue())) {
                errorDescription.append("Platform release must be related to platform!");
            } else if (request.getEntity().getType().equals(ReleaseType.PLATFORM.getValue()) && request.getEntity().getGameId() != null) {
                errorDescription.append("Platform release can't be related to game!");
            } else if (request.getEntity().getGameId() == null && request.getEntity().getType().equals(ReleaseType.GAME.getValue())) {
                errorDescription.append("Game release must be related to game!");
            } else if (request.getEntity().getGameId() != null) {
                EntityRequest<Long> gameRequest = new EntityRequest<>(request.getEntity().getRegionId(), request);
                gameRequestValidation.validateIfGameExists(gameRequest, validationRuleMessage);
            } else if (request.getEntity().getPlatformId() != null) {
                EntityRequest<Long> platformRequest = new EntityRequest<>(request.getEntity().getRegionId(), request);
                platformRequestValidation.validatePlatformExists(platformRequest, validationRuleMessage);
            }

            if (request.getEntity().getDeveloperId() != null) {
                EntityRequest<Long> developerRequest = new EntityRequest<>(request.getEntity().getDeveloperId(), request);
                companyRequestValidation.validateCompanyExists(developerRequest, validationRuleMessage);
            }
            if (request.getEntity().getPublisherId() != null) {
                EntityRequest<Long> publisherRequest = new EntityRequest<>(request.getEntity().getPublisherId(), request);
                companyRequestValidation.validateCompanyExists(publisherRequest, validationRuleMessage);
            }

            validationResponse = requestValidator.createResponse(request, errorDescription);
        }
        return validationResponse;
    }
}
