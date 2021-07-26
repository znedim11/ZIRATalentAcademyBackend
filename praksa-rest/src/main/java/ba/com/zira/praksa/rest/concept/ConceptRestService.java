/**
 *
 */
package ba.com.zira.praksa.rest.concept;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.ConceptService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.character.CharacterResponse;
import ba.com.zira.praksa.api.model.concept.ConceptCreateRequest;
import ba.com.zira.praksa.api.model.concept.ConceptResponse;
import ba.com.zira.praksa.api.model.concept.ConceptSearchRequest;
import ba.com.zira.praksa.api.model.concept.ConceptUpdateRequest;
import ba.com.zira.praksa.api.model.game.GameOverviewResponse;
import ba.com.zira.praksa.api.model.location.Location;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.api.model.person.Person;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author zira
 *
 */

@Api(tags = "concept")
@RestController
@RequestMapping(value = "concept")
public class ConceptRestService {

    @Autowired
    ConceptService conceptService;

    @ApiOperation(value = "Find Concepts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<ConceptResponse> find(@RequestParam(required = false) final String pagination,
            @RequestParam(required = false) final String filter, @RequestParam(required = false) final String sorting) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);
        request.setFilterExpression(filter);
        request.setSorting(sorting);

        return conceptService.find(request);
    }

    @ApiOperation(value = "Get Concept by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<ConceptResponse> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return conceptService.findById(request);
    }

    @ApiOperation(value = "Create Concept", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<ConceptResponse> create(@RequestBody EntityRequest<ConceptCreateRequest> request) throws ApiException {
        return conceptService.create(request);
    }

    @ApiOperation(value = "Update Concept", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<ConceptResponse> update(@PathVariable final Long id,
            @RequestBody final EntityRequest<ConceptUpdateRequest> request) throws ApiException {

        final ConceptUpdateRequest concept = request.getEntity();
        if (!Objects.isNull(concept)) {
            concept.setId(id);
        }

        return conceptService.update(request);
    }

    @ApiOperation(value = "Delete Concept by Id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{id}")
    public PayloadResponse<String> delete(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return conceptService.delete(request);
    }

    @ApiOperation(value = "Get Games by Concept.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/games")
    public ListPayloadResponse<GameOverviewResponse> getGamesByConcept(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return conceptService.getGamesByConcept(request);
    }

    @ApiOperation(value = "Get Persons by Concept.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/persons")
    public ListPayloadResponse<Person> getPersonsByConcept(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return conceptService.getPersonsByConcept(request);
    }

    @ApiOperation(value = "Get Concept names by Ids.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lovs")
    public ListPayloadResponse<LoV> getLoVs(@RequestParam(required = false) final List<Long> ids) throws ApiException {

        final ListRequest<Long> request = new ListRequest<>();
        request.setList(ids);

        return conceptService.getLoVs(request);
    }

    @ApiOperation(value = "Get Objects by Concept.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/objects")
    public ListPayloadResponse<ObjectResponse> getObjectsByConcept(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return conceptService.getObjectsByConcept(request);
    }

    @ApiOperation(value = "Get Characters by Concept.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/characters")
    public ListPayloadResponse<CharacterResponse> getCharactersByConcept(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return conceptService.getCharactersByConcept(request);
    }

    @ApiOperation(value = "Get Locations by Concept.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/locations")
    public ListPayloadResponse<Location> getLocationsByConcept(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return conceptService.getLocationsByConcept(request);
    }

    @ApiOperation(value = "Get number of Games by Concept.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/gamecount")
    public PayloadResponse<Long> getNumberOfGamesForConcept(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return conceptService.getNumberOfGamesByConcept(request);
    }

    @ApiOperation(value = "Search Concepts.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/search")
    public ListPayloadResponse<ConceptResponse> searchConcepts(@RequestParam(required = false) final String name,
            @RequestParam(required = false) final String sortBy, @RequestParam(required = false) final List<Long> gameIds,
            @RequestParam(required = false) final List<Long> characterIds) throws ApiException {

        final EntityRequest<ConceptSearchRequest> request = new EntityRequest<>();
        final ConceptSearchRequest entity = new ConceptSearchRequest();
        entity.setName(name);
        entity.setGameIds(gameIds);
        entity.setCharacterIds(characterIds);
        entity.setSortBy(sortBy);

        request.setEntity(entity);

        return conceptService.searchConcepts(request);
    }

    @ApiOperation(value = "Get oldest release date by Concept.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/releasedate")
    public PayloadResponse<LocalDateTime> getOldestReleaseDateByConcept(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return conceptService.getOldestReleaseDateByConcept(request);
    }

    @ApiOperation(value = "Get Concept not Connected to ...", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lov-not-connected")
    public ListPayloadResponse<LoV> getLoVNotConnectedTo(@RequestParam(required = true) final String type,
            @RequestParam(required = true) final Long id) throws ApiException {

        final EntityRequest<LoV> request = new EntityRequest<>();
        request.setEntity(new LoV(id, type));

        return conceptService.getLoVsNotConnectedTo(request);
    }

}
