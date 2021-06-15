package ba.com.zira.praksa.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.SpelEvaluationException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.model.game.Game;
import ba.com.zira.praksa.core.validation.GameRequestValidation;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.test.configuration.BasicTestConfiguration;

public class GameValidationTest extends BasicTestConfiguration {

    private static final String TEMPLATE_CODE = "TEST_1";

    @Autowired
    RequestValidator requestValidator;

    GameDAO gameDAO;
    GameRequestValidation validation;

    @BeforeMethod
    public void beforeMethod() throws ApiException {
        this.gameDAO = Mockito.mock(GameDAO.class);
        this.validation = new GameRequestValidation(requestValidator, gameDAO);
    }

    @Test
    public void validateCreateRequestMissingMadatoryFields() {
        ValidationResponse validationResponse = validation.validateUpdateGameRequest(new EntityRequest<>(new Game()), "create");

        assertEquals(validationResponse.getCode(), ResponseCode.REQUEST_INVALID);
        Mockito.verifyZeroInteractions(gameDAO);
        assertEquals(validationResponse.getDescription(),
                "EntityRequest : entity:  name required! entity:  type required! entity:  status required! entity:  validFrom required! ");
    }

    @Test
    public void validateCreateRequestMissingEntity() {
        EntityRequest<Game> request = new EntityRequest<>();
        assertThrows(SpelEvaluationException.class, () -> validation.validateUpdateGameRequest(request, "create"));
        Mockito.verifyZeroInteractions(gameDAO);
    }

    /**
     * Update
     */

    @Test
    public void validateUpdateRequestSampleNotFound() {
        Mockito.when(gameDAO.existsByPK(1L)).thenReturn(false);

        ValidationResponse validationResponse = validation.validateUpdateGameRequest(new EntityRequest<>(new Game()), "update");

        assertEquals(validationResponse.getCode(), ResponseCode.REQUEST_INVALID);
        assertEquals(validationResponse.getDescription(), "message");
        Mockito.verify(gameDAO).existsByPK(1L);
    }

}
