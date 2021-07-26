package ba.com.zira.praksa.rest.person;

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
import ba.com.zira.praksa.api.PersonService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.character.CharacterResponse;
import ba.com.zira.praksa.api.model.concept.ConceptResponse;
import ba.com.zira.praksa.api.model.game.GameResponse;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.api.model.person.Person;
import ba.com.zira.praksa.api.model.person.PersonCreateRequest;
import ba.com.zira.praksa.api.model.person.PersonUpdateRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "person")
@RestController
@RequestMapping(value = "person")
public class PersonRestService {

    @Autowired
    private PersonService personService;

    @ApiOperation(value = "Find Person", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<Person> find(@RequestParam(required = false) final String pagination,
            @RequestParam(required = false) final String sorting, @RequestParam(required = false) final String filter) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);
        request.setFilterExpression(filter);
        request.setSorting(sorting);
        return personService.find(request);
    }

    @ApiOperation(value = "Get Person by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<Person> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return personService.findById(request);
    }

    @ApiOperation(value = "Create Person", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<Person> createPerson(@RequestBody EntityRequest<PersonCreateRequest> request) throws ApiException {
        return personService.create(request);
    }

    @ApiOperation(value = "Update Person", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<Person> update(@PathVariable final String id, @RequestBody final EntityRequest<PersonUpdateRequest> request)
            throws ApiException {

        final PersonUpdateRequest person = request.getEntity();
        person.setId(Long.decode(id));

        return personService.update(request);
    }

    @ApiOperation(value = "Delete Person by Id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        personService.delete(request);
    }

    @ApiOperation(value = "Get Games for Person", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/games")
    public ListPayloadResponse<GameResponse> getGamesForPerson(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return personService.getGamesForPerson(request);
    }

    @ApiOperation(value = "Get Concepts for Person", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/concepts")
    public ListPayloadResponse<ConceptResponse> getConceptsForPerson(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return personService.getConceptsForPerson(request);
    }

    @ApiOperation(value = "Get Characters for Person", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/characters")
    public ListPayloadResponse<CharacterResponse> getCharactersForPerson(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return personService.getCharactersForPerson(request);
    }

    @ApiOperation(value = "Get Objects for Person", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}/objects")
    public ListPayloadResponse<ObjectResponse> getObjectsForPerson(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return personService.getObjectsForPerson(request);
    }

    @ApiOperation(value = "Get Person names by Ids.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lovs")
    public ListPayloadResponse<LoV> getLoVs(@RequestParam(required = false) final List<Long> ids) throws ApiException {

        final ListRequest<Long> request = new ListRequest<>();
        request.setList(ids);

        return personService.getLoVs(request);
    }

    @ApiOperation(value = "Get Persons not Connected to ...", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lov-not-connected")
    public ListPayloadResponse<LoV> getLoVNotConnectedTo(@RequestParam(required = true) final String type,
            @RequestParam(required = true) final Long id) throws ApiException {

        final EntityRequest<LoV> request = new EntityRequest<>();
        request.setEntity(new LoV(id, type));

        return personService.getLoVsNotConnectedTo(request);
    }

}
