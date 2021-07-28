package ba.com.zira.praksa.rest.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EmptyRequest;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.GameService;
import ba.com.zira.praksa.api.ReleaseService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.character.CharacterResponse;
import ba.com.zira.praksa.api.model.concept.ConceptResponse;
import ba.com.zira.praksa.api.model.enums.ReleaseType;
import ba.com.zira.praksa.api.model.feature.FeatureResponse;
import ba.com.zira.praksa.api.model.game.GameCreateRequest;
import ba.com.zira.praksa.api.model.game.GameOverviewResponse;
import ba.com.zira.praksa.api.model.game.GameResponse;
import ba.com.zira.praksa.api.model.game.GameSearchRequest;
import ba.com.zira.praksa.api.model.game.GameUpdateRequest;
import ba.com.zira.praksa.api.model.game.dlc.DlcAnalysisReport;
import ba.com.zira.praksa.api.model.gamefeature.GameFeatureCreateRequest;
import ba.com.zira.praksa.api.model.gamefeature.GameFeatureResponse;
import ba.com.zira.praksa.api.model.location.Location;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.api.model.person.Person;
import ba.com.zira.praksa.api.model.release.ReleaseRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "game")
@RestController
@RequestMapping(value = "game")
public class GameRestService {

    @Autowired
    private GameService gameService;
    @Autowired
    private ReleaseService releaseService;

    @ApiOperation(value = "Find Games", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<GameResponse> find(@RequestParam(required = false) final String pagination) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);
        return gameService.find(request);
    }

    @ApiOperation(value = "Get Game by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<GameResponse> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return gameService.findById(request);
    }

    @ApiOperation(value = "Create Game", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<GameResponse> createGame(@RequestBody EntityRequest<GameCreateRequest> request) throws ApiException {
        return gameService.create(request);
    }

    @ApiOperation(value = "Update Game", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<GameResponse> update(@PathVariable final Long id, @RequestBody final EntityRequest<GameUpdateRequest> request)
            throws ApiException {

        final GameUpdateRequest sample = request.getEntity();
        sample.setId(id);

        return gameService.update(request);
    }

    @ApiOperation(value = "Delete Game by Id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        gameService.delete(request);
    }

    @ApiOperation(value = "Get Features by Game Id.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/get-features/{id}")
    public PagedPayloadResponse<FeatureResponse> getFeatures(@PathVariable final Long id) throws ApiException {
        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return gameService.getFeaturesByGame(request);
    }

    @ApiOperation(value = "Add Feature", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/add-feature")
    public PayloadResponse<GameFeatureResponse> addFeature(@RequestBody EntityRequest<GameFeatureCreateRequest> request)
            throws ApiException {
        return gameService.addFeature(request);
    }

    @ApiOperation(value = "Remove Feature", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/remove-feature/{uuid}")
    public void removeFeature(@PathVariable final String uuid) throws ApiException {
        final EntityRequest<String> request = new EntityRequest<>();
        request.setEntity(uuid);

        gameService.removeFeature(request);
    }

    @ApiOperation(value = "Add Release", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/release/add")
    public PayloadResponse<String> addReleaseGame(@RequestBody final EntityRequest<ReleaseRequest> request) throws ApiException {
        final ReleaseRequest addReleaseRequest = request.getEntity();
        addReleaseRequest.setType(ReleaseType.GAME.getValue());
        return releaseService.addRelease(request);
    }

    @ApiOperation(value = "Get Concepts by Game", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/concepts")
    public ListPayloadResponse<ConceptResponse> getGamesByGame(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return gameService.getConceptsByGame(request);
    }

    @ApiOperation(value = "Get Persons by Game", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/persons")
    public ListPayloadResponse<Person> getPersonsByGame(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return gameService.getPersonsByGame(request);
    }

    @ApiOperation(value = "Get Game names by Ids", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lovs")
    public PagedPayloadResponse<LoV> getLoVs(@RequestParam(required = false) final String pagination,
            @RequestParam(required = false) final String filter) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setPagination(pagination);
        request.setFilterExpression(filter);

        return gameService.getLoVs(request);
    }

    @ApiOperation(value = "Get all Main Games", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/main")
    public ListPayloadResponse<LoV> getMainGames() throws ApiException {
        EmptyRequest request = new EmptyRequest();
        return gameService.getMainGames(request);
    }

    @ApiOperation(value = "Get Objects by Game", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/objects")
    public ListPayloadResponse<ObjectResponse> getObjectsByGame(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return gameService.getObjectsByGame(request);
    }

    @ApiOperation(value = "Get Characters by Game", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/characters")
    public ListPayloadResponse<CharacterResponse> getCharactersByGame(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return gameService.getCharactersByGame(request);
    }

    @ApiOperation(value = "Get Locations by Game", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/locations")
    public ListPayloadResponse<Location> getLocationsByGame(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return gameService.getLocationsByGame(request);
    }

    @ApiOperation(value = "Get number of Releases by Game", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/releasecount")
    public PayloadResponse<Long> getNumberOfReleasesByGame(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return gameService.getNumberOfReleasesByGame(request);
    }

    @ApiOperation(value = "Get Game Overview", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/overview")
    public PayloadResponse<GameOverviewResponse> getOverview(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return gameService.getOverview(request);
    }

    @ApiOperation(value = "Get DLC Analysis Report", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/dlc-analysis-report")
    public PayloadResponse<DlcAnalysisReport> dlcAnalysisReport() throws ApiException {
        EmptyRequest request = new EmptyRequest();
        return gameService.dlcAnalysisReport(request);
    }

    @ApiOperation(value = "Get Games not Connected to ...", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lov-not-connected")
    public PagedPayloadResponse<LoV> getLoVNotConnectedTo(@RequestParam(required = true) final String type,
            @RequestParam(required = true) final Long id, @RequestParam(required = false) final String pagination,
            @RequestParam(required = false) final String filter) throws ApiException {

        final SearchRequest<LoV> request = new SearchRequest<>();
        request.setEntity(new LoV(id, type));
        request.setPagination(pagination);
        request.setFilterExpression(filter);

        return gameService.getLoVsNotConnectedTo(request);
    }

    @ApiOperation(value = "Search Games.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/search")
    public PagedPayloadResponse<GameResponse> searchGames(@RequestParam(required = false) final String name,
            @RequestParam(required = false) final String genre, @RequestParam(required = false) final String releasedBefore,
            @RequestParam(required = false) final String releasedAfter, @RequestParam(required = false) final List<Long> regionIds,
            @RequestParam(required = false) final List<Long> featureIds, @RequestParam(required = false) final Long developerId,
            @RequestParam(required = false) final Long publisherId, @RequestParam(required = false) final String pagination)
            throws ApiException {

        final SearchRequest<GameSearchRequest> request = new SearchRequest<>();
        final GameSearchRequest entity = new GameSearchRequest(releasedBefore, releasedAfter, name, genre, developerId, publisherId,
                regionIds, featureIds);
        request.setEntity(entity);
        request.setPagination(pagination);

        return gameService.searchGames(request);
    }

    @ApiOperation(value = "Get all genres", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/genres")
    public ListPayloadResponse<String> getGenres() throws ApiException {
        SearchRequest<Long> request = new SearchRequest<>();

        return gameService.getGenres(request);
    }
}
