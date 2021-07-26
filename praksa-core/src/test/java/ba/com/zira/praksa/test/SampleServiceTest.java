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
import ba.com.zira.praksa.api.MediaService;
import ba.com.zira.praksa.api.MediaStoreService;
import ba.com.zira.praksa.api.model.game.Game;
import ba.com.zira.praksa.api.model.game.GameResponse;
import ba.com.zira.praksa.core.impl.GameServiceImpl;
import ba.com.zira.praksa.core.utils.LookupService;
import ba.com.zira.praksa.core.validation.FeatureRequestValidation;
import ba.com.zira.praksa.core.validation.GameRequestValidation;
import ba.com.zira.praksa.dao.CompanyDAO;
import ba.com.zira.praksa.dao.FeatureDAO;
import ba.com.zira.praksa.dao.FranchiseDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.GameFeatureDAO;
import ba.com.zira.praksa.dao.MediaDAO;
import ba.com.zira.praksa.dao.MediaStoreDAO;
import ba.com.zira.praksa.dao.PlatformDAO;
import ba.com.zira.praksa.dao.ReleaseDAO;
import ba.com.zira.praksa.mapper.CharacterMapper;
import ba.com.zira.praksa.mapper.ConceptMapper;
import ba.com.zira.praksa.mapper.FeatureMapper;
import ba.com.zira.praksa.mapper.GameFeatureMapper;
import ba.com.zira.praksa.mapper.GameMapper;
import ba.com.zira.praksa.mapper.LocationMapper;
import ba.com.zira.praksa.mapper.ObjectMapper;
import ba.com.zira.praksa.mapper.PersonMapper;
import ba.com.zira.praksa.mapper.PlatformMapper;
import ba.com.zira.praksa.mapper.ReleaseMapper;
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
    private GameRequestValidation gameRequestValidation;
    private FeatureDAO featureDAO;
    private GameFeatureDAO gameFeatureDAO;
    private ReleaseDAO releaseDAO;
    private PlatformDAO platformDAO;
    private CompanyDAO companyDAO;
    private FranchiseDAO franchiseDAO;
    private MediaStoreDAO mediaStoreDAO;
    private MediaDAO mediaDAO;
    private RequestValidator requestValidator;
    private GameRequestValidation sampleRequestValidation;
    private FeatureRequestValidation featureRequestValidation;
    private GameService gameService;
    private MediaStoreService mediaStoreService;
    private MediaService mediaService;

    private ConceptMapper conceptMapper;
    private PersonMapper personMapper;
    private ObjectMapper objectMapper;
    private CharacterMapper characterMapper;
    private LocationMapper locationMapper;
    private PlatformMapper platformMapper;
    private ReleaseMapper releaseMapper;
    private GameMapper gameMapper;
    private LookupService lookupService;

    @BeforeMethod
    public void beforeMethod() throws ApiException {
        this.requestValidator = Mockito.mock(RequestValidator.class);
        this.gameDAO = Mockito.mock(GameDAO.class);
        this.featureDAO = Mockito.mock(FeatureDAO.class);
        this.gameFeatureDAO = Mockito.mock(GameFeatureDAO.class);
        this.releaseDAO = Mockito.mock(ReleaseDAO.class);
        this.companyDAO = Mockito.mock(CompanyDAO.class);
        this.franchiseDAO = Mockito.mock(FranchiseDAO.class);
        this.platformDAO = Mockito.mock(PlatformDAO.class);
        this.sampleRequestValidation = Mockito.mock(GameRequestValidation.class);
        this.featureRequestValidation = Mockito.mock(FeatureRequestValidation.class);
        this.conceptMapper = Mockito.mock(ConceptMapper.class);
        this.personMapper = Mockito.mock(PersonMapper.class);
        this.objectMapper = Mockito.mock(ObjectMapper.class);
        this.characterMapper = Mockito.mock(CharacterMapper.class);
        this.locationMapper = Mockito.mock(LocationMapper.class);
        this.platformMapper = Mockito.mock(PlatformMapper.class);
        this.releaseMapper = Mockito.mock(ReleaseMapper.class);
        this.lookupService = Mockito.mock(LookupService.class);
        this.gameRequestValidation = Mockito.mock(GameRequestValidation.class);
        this.gameMapper = Mockito.mock(GameMapper.class);
        this.gameService = new GameServiceImpl(requestValidator, gameRequestValidation, featureRequestValidation, gameDAO, featureDAO,
                gameFeatureDAO, releaseDAO, platformDAO, companyDAO, franchiseDAO, mediaStoreDAO, mediaDAO, lookupService, gameMapper,
                conceptMapper, personMapper, objectMapper, characterMapper, locationMapper, featureMapper, gameFeatureMapper, releaseMapper,
                platformMapper, mediaStoreService, mediaService);
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
