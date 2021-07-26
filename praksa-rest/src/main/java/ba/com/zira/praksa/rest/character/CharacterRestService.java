package ba.com.zira.praksa.rest.character;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.CharacterService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.character.CharacterCreateRequest;
import ba.com.zira.praksa.api.model.character.CharacterSearchRequest;
import ba.com.zira.praksa.api.model.character.CharacterSearchResponse;
import ba.com.zira.praksa.api.model.character.CharacterUpdateRequest;
import ba.com.zira.praksa.api.model.character.CompleteCharacterResponse;
import ba.com.zira.praksa.api.model.concept.ConceptResponse;
import ba.com.zira.praksa.api.model.game.GameCharacterResponse;
import ba.com.zira.praksa.api.model.location.Location;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.api.model.person.Person;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author zira
 *
 */

@Api(tags = "character")
@RestController
@RequestMapping(value = "character")
public class CharacterRestService {

    @Autowired
    private CharacterService characterService;

    @ApiOperation(value = "Search characters.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/search")
    public PagedPayloadResponse<CharacterSearchResponse> searchCharacters(@RequestParam(required = false) final String name,
            @RequestParam(required = false) final String gender, @RequestParam(required = false) final String dob,
            @RequestParam(required = false) final String dobCondition, @RequestParam(required = false) final String sortBy)
            throws ApiException {
        CharacterSearchRequest characterSearchRequest = new CharacterSearchRequest();

        characterSearchRequest.setName(name);
        characterSearchRequest.setDobCondition(dobCondition);
        characterSearchRequest.setGender(gender);
        characterSearchRequest.setSortBy(sortBy);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        characterSearchRequest.setDob(dob != null ? LocalDate.parse(dob, formatter).atStartOfDay() : null);

        final EntityRequest<CharacterSearchRequest> request = new EntityRequest<>();
        request.setEntity(characterSearchRequest);

        return characterService.searchCharacters(request);
    }

    @ApiOperation(value = "Get Character by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<CompleteCharacterResponse> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return characterService.findById(request);
    }

    @ApiOperation(value = "Create Character", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<CompleteCharacterResponse> create(@RequestBody EntityRequest<CharacterCreateRequest> request)
            throws ApiException {

        return characterService.create(request);
    }

    @ApiOperation(value = "Update Character", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<CompleteCharacterResponse> update(@PathVariable final Long id,
            @RequestBody EntityRequest<CharacterUpdateRequest> request) throws ApiException {

        CharacterUpdateRequest characterUpdateRequest = request.getEntity();
        characterUpdateRequest.setId(id);

        return characterService.update(request);
    }

    @ApiOperation(value = "Delete Character by Id", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{id}")
    public PayloadResponse<String> delete(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return characterService.delete(request);
    }

    @ApiOperation(value = "Get Character names by Ids.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lovs")
    public ListPayloadResponse<LoV> getLoVs(@RequestParam(required = false) final List<Long> ids) throws ApiException {

        final ListRequest<Long> request = new ListRequest<>();
        request.setList(ids);

        return characterService.getLoVs(request);
    }

    @ApiOperation(value = "Get Games by Character", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/games")
    public ListPayloadResponse<GameCharacterResponse> getCharacterGames(@PathVariable final Long id) throws ApiException {
        EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return characterService.getGamesForCharacter(request);
    }

    @ApiOperation(value = "Get Concepts by Character.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/concepts")
    public ListPayloadResponse<ConceptResponse> getConceptsByConcept(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return characterService.getConceptsByCharacter(request);
    }

    @ApiOperation(value = "Get Persons by Character.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/persons")
    public ListPayloadResponse<Person> getPersonsByConcept(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return characterService.getPersonsByCharacter(request);
    }

    @ApiOperation(value = "Get Objects by Character.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/objects")
    public ListPayloadResponse<ObjectResponse> getObjectsByConcept(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return characterService.getObjectsByCharacter(request);
    }

    @ApiOperation(value = "Get Locations by Character.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/locations")
    public ListPayloadResponse<Location> getLocationsByConcept(@PathVariable final Long id) throws ApiException {

        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return characterService.getLocationsByCharacter(request);
    }

    @ApiOperation(value = "Get Character not Connected to ...", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lov-not-connected")
    public ListPayloadResponse<LoV> getLoVNotConnectedTo(@RequestParam(required = true) final String type,
            @RequestParam(required = true) final Long id) throws ApiException {

        final EntityRequest<LoV> request = new EntityRequest<>();
        request.setEntity(new LoV(id, type));

        return characterService.getLoVsNotConnectedTo(request);
    }
}
