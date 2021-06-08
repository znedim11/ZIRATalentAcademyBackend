/**
 *
 */
package ba.com.zira.praksa.rest.game;

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
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.ConceptService;
import ba.com.zira.praksa.api.model.concept.Concept;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author irma
 *
 */

@Api(tags = "concept")
@RestController
@RequestMapping(value = "concept")
public class ConceptRestService {

    @Autowired
    private ConceptService conceptService;

    @ApiOperation(value = "Find Concepts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<Concept> find(@RequestParam(required = false) final String pagination) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);
        return conceptService.find(request);
    }

    @ApiOperation(value = "Get Concept by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<Concept> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return conceptService.findById(request);
    }

    @ApiOperation(value = "Create Concept", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<Concept> createGame(@RequestBody EntityRequest<Concept> request) throws ApiException {
        return conceptService.create(request);
    }

    @ApiOperation(value = "Update Concept", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<Concept> update(@PathVariable final String id, @RequestBody final EntityRequest<Concept> request)
            throws ApiException {

        final Concept sample = request.getEntity();
        sample.setId(Long.decode(id));

        return conceptService.update(request);
    }

    @ApiOperation(value = "Delete Concept by Id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        conceptService.delete(request);
    }
}
