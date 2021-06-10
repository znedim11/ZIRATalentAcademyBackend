/**
 *
 */
package ba.com.zira.praksa.rest.concept;

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
import ba.com.zira.praksa.api.model.concept.ConceptCreateRequest;
import ba.com.zira.praksa.api.model.concept.ConceptResponse;
import ba.com.zira.praksa.api.model.concept.ConceptUpdateRequest;
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
    private ConceptService conceptService;

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
    public PayloadResponse<ConceptResponse> createGame(@RequestBody EntityRequest<ConceptCreateRequest> request) throws ApiException {
        return conceptService.create(request);
    }

    @ApiOperation(value = "Update Concept", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<ConceptResponse> update(@PathVariable final String id,
            @RequestBody final EntityRequest<ConceptUpdateRequest> request) throws ApiException {

        final ConceptUpdateRequest concept = request.getEntity();
        concept.setId(Long.decode(id));

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
