package ba.com.zira.praksa.core.validation;

import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.GameService;
import ba.com.zira.praksa.api.model.game.Game;
import ba.com.zira.praksa.api.model.gamefeature.GameFeatureCreateRequest;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.GameFeatureDAO;

/**
 * SampleRequestValidation is used for validation of {@link GameService}
 * requests.<br>
 * e.g. database validation needed
 *
 * @author zira
 *
 */
@Component("gameRequestValidation")
public class GameRequestValidation {

    RequestValidator requestValidator;
    GameDAO gameDAO;
    GameFeatureDAO gameFeatureDAO;

    public GameRequestValidation(RequestValidator requestValidator, GameDAO gameDAO, GameFeatureDAO gameFeatureDAO) {
        super();
        this.requestValidator = requestValidator;
        this.gameDAO = gameDAO;
        this.gameFeatureDAO = gameFeatureDAO;
    }

    /**
     * Validates update Game plan from {@link GameService}.
     *
     * @param request
     *            the {@link EntityRequest} to validate.
     * @param validationRuleMessage
     *            name of the validation rule that is going to be used for
     *            validating {@link Game}
     *
     * @return {@link ValidationResponse}
     */
    public ValidationResponse validateUpdateGameRequest(final EntityRequest<Game> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);
        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!gameDAO.existsByPK(request.getEntity().getId())) {
                errorDescription.append("Sample with id: ").append(request.getEntity().getId()).append(" does not exist !");
            }
            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

    /**
     * Validates if Game exists in database.
     *
     * @param request
     *            the {@link EntityRequest} to validate.
     * @param validationRuleMessage
     *            name of the validation rule that is going to be used for
     *            validating Game Id
     *
     * @return {@link ValidationResponse}
     */
    public ValidationResponse validateIfGameExists(final EntityRequest<Long> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);

        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!gameDAO.existsByPK(request.getEntity())) {
                errorDescription.append("Game with id: ").append(request.getEntity()).append(" does not exist !");
            }

            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

    /**
     * Validates if Game has a feature.
     *
     * @param request
     *            the {@link EntityRequest} to validate.
     * @param validationRuleMessage
     *            name of the validation rule that is going to be used for
     *            validating GameFeature Id
     *
     * @return {@link ValidationResponse}
     */
    public ValidationResponse validateIfGameHasFeature(final EntityRequest<String> request, final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);

        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            if (!gameFeatureDAO.existsByPK(request.getEntity())) {
                errorDescription.append("Game does not have chosen feature !");
            }

            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

    /**
     * Validates if Game has a feature.
     *
     * @param request
     *            the {@link EntityRequest} to validate.
     * @param validationRuleMessage
     *            name of the validation rule that is going to be used for
     *            validating GameFeature Id
     *
     * @return {@link ValidationResponse}
     */
    public ValidationResponse validateIfGameAlreadyHasFeature(final EntityRequest<GameFeatureCreateRequest> request,
            final String validationRuleMessage) {
        ValidationResponse validationResponse = requestValidator.validate(request, validationRuleMessage);

        if (validationResponse.getResponseCode() == ResponseCode.OK.getCode()) {
            StringBuilder errorDescription = new StringBuilder();
            GameFeatureCreateRequest gfEntity = request.getEntity();
            if (gameFeatureDAO.checkIfRelationExists(gfEntity.getGameId(), gfEntity.getFeatureId())) {
                errorDescription.append("Game already has chosen feature !");
            }

            validationResponse = requestValidator.createResponse(request, errorDescription);
        }

        return validationResponse;
    }

    public ValidationResponse validateGameExist(final SearchRequest<Long> rq, String validationRuleMessage) {
        ValidationResponse validationResponse = new ValidationResponse(rq, ResponseCode.OK);
        StringBuilder errorDescription = new StringBuilder();
        if (!gameDAO.existsByPK(rq.getEntity())) {
            errorDescription.append("Game with id: ").append(rq.getEntity()).append(" does not exist!");
        }
        validationResponse = requestValidator.createResponse(rq, errorDescription);
        return validationResponse;
    }
}
