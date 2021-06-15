package ba.com.zira.praksa.rest.externalreview;

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
import ba.com.zira.praksa.api.ExternalReviewService;
import ba.com.zira.praksa.api.model.externalreview.ExternalReview;
import ba.com.zira.praksa.api.model.externalreview.ExternalReviewCreateRequest;
import ba.com.zira.praksa.api.model.externalreview.ExternalReviewUpdateRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "externalReview")
@RestController
@RequestMapping(value = "externalReview")
public class ExternalReviewRestService {

    @Autowired
    ExternalReviewService externalReviewService;

    @ApiOperation(value = "Find a review", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<ExternalReview> find(@RequestParam(required = false) final String pagination,
            @RequestParam(required = false) final String filter, @RequestParam(required = false) final String sorting) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);
        request.setFilterExpression(filter);
        request.setSorting(sorting);

        return externalReviewService.find(request);
    }

    @ApiOperation(value = "Get Concept by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<ExternalReview> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return externalReviewService.findById(request);
    }

    @ApiOperation(value = "Create a review", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<ExternalReview> createGame(@RequestBody EntityRequest<ExternalReviewCreateRequest> request) throws ApiException {
        return externalReviewService.create(request);
    }

    @ApiOperation(value = "Update a review", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<ExternalReview> update(@PathVariable final String id,
            @RequestBody(required = true) final EntityRequest<ExternalReviewUpdateRequest> request) throws ApiException {

        final ExternalReviewUpdateRequest concept = request.getEntity();
        concept.setId(Long.decode(id));

        return externalReviewService.update(request);
    }

    @ApiOperation(value = "Delete a review by Id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        externalReviewService.delete(request);
    }
}
