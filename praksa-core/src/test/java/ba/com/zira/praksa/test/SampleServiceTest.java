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
import ba.com.zira.praksa.core.validation.FeatureRequestValidation;
import ba.com.zira.praksa.core.validation.GameRequestValidation;
import ba.com.zira.praksa.dao.FeatureDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.GameFeatureDAO;
import ba.com.zira.praksa.mapper.CharacterMapper;
import ba.com.zira.praksa.mapper.ConceptMapper;
import ba.com.zira.praksa.mapper.FeatureMapper;
import ba.com.zira.praksa.mapper.GameFeatureMapper;
import ba.com.zira.praksa.mapper.GameMapper;
import ba.com.zira.praksa.mapper.LocationMapper;
import ba.com.zira.praksa.mapper.ObjectMapper;
import ba.com.zira.praksa.mapper.PersonMapper;
import ba.com.zira.praksa.test.configuration.ApplicationTestConfiguration;
import ba.com.zira.praksa.test.configuration.BasicTestConfiguration;

@ContextConfiguration(classes = ApplicationTestConfiguration.class)
public class SampleServiceTest extends BasicTestConfiguration {

    @Autowired
    private GameMapper sampleMapper;
    @Autowired
    private FeatureMapper featureMapper;
    @Autowired
    private GameFeatureMapper gameFeatureMapper;

    private GameDAO gameDAO;
    private FeatureDAO featureDAO;
    private GameFeatureDAO gameFeatureDAO;
    private RequestValidator requestValidator;
    private GameRequestValidation sampleRequestValidation;
    private FeatureRequestValidation featureRequestValidation;
    private GameService gameService;

    private ConceptMapper conceptMapper;
    private PersonMapper personMapper;
    private ObjectMapper objectMapper;
    private CharacterMapper characterMapper;
    private LocationMapper locationMapper;

    @BeforeMethod
    public void beforeMethod() throws ApiException {
        this.requestValidator = Mockito.mock(RequestValidator.class);
        this.gameDAO = Mockito.mock(GameDAO.class);
        this.featureDAO = Mockito.mock(FeatureDAO.class);
        this.gameFeatureDAO = Mockito.mock(GameFeatureDAO.class);
        this.sampleRequestValidation = Mockito.mock(GameRequestValidation.class);
        this.featureRequestValidation = Mockito.mock(FeatureRequestValidation.class);
        this.conceptMapper = Mockito.mock(ConceptMapper.class);
        this.personMapper = Mockito.mock(PersonMapper.class);
        this.objectMapper = Mockito.mock(ObjectMapper.class);
        this.characterMapper = Mockito.mock(CharacterMapper.class);
        this.locationMapper = Mockito.mock(LocationMapper.class);
        this.gameService = new GameServiceImpl(requestValidator, sampleRequestValidation, featureRequestValidation, gameDAO, featureDAO,
                gameFeatureDAO, sampleMapper, featureMapper, gameFeatureMapper, conceptMapper, personMapper, objectMapper, characterMapper,
                locationMapper);
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
