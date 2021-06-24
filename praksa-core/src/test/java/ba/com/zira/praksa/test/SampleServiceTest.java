package ba.com.zira.praksa.test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.GameService;
import ba.com.zira.praksa.api.model.game.Game;
import ba.com.zira.praksa.api.model.game.GameResponse;
import ba.com.zira.praksa.core.impl.GameServiceImpl;
import ba.com.zira.praksa.core.validation.GameRequestValidation;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.mapper.GameMapper;
import ba.com.zira.praksa.test.configuration.ApplicationTestConfiguration;
import ba.com.zira.praksa.test.configuration.BasicTestConfiguration;

@ContextConfiguration(classes = ApplicationTestConfiguration.class)
public class SampleServiceTest extends BasicTestConfiguration {

    @Autowired
    private GameMapper sampleMapper;

    private GameDAO gameDAO;
    private RequestValidator requestValidator;
    private GameRequestValidation sampleRequestValidation;
    private GameService gameService;

    @BeforeMethod
    public void beforeMethod() throws ApiException {
        this.requestValidator = Mockito.mock(RequestValidator.class);
        this.gameDAO = Mockito.mock(GameDAO.class);
        this.sampleRequestValidation = Mockito.mock(GameRequestValidation.class);
        this.gameService = new GameServiceImpl(requestValidator, gameDAO, sampleMapper);
    }

    @Test
    public void createTemplateMandatoryFields() {
        try {
            Game sampleModel = new Game();
            SearchRequest<String> request = new SearchRequest();
            PagedPayloadResponse<GameResponse> response = gameService.find(request);

            Mockito.verify(gameDAO).persist(Mockito.any());
            // assert
        } catch (ApiException e) {
            Assert.fail();
        }
    }

}
