package ba.com.zira.praksa.core.validation;

import org.springframework.stereotype.Component;

import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.GameService;
import ba.com.zira.praksa.api.model.game.Game;
import ba.com.zira.praksa.dao.GameDAO;

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

    private RequestValidator requestValidator;
    private GameDAO gameDAO;

    public GameRequestValidation(final RequestValidator requestValidator, GameDAO gameDAO) {
        this.requestValidator = requestValidator;
        this.gameDAO = gameDAO;
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

}
