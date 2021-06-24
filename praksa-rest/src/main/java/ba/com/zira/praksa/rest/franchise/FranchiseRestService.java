package ba.com.zira.praksa.rest.franchise;

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
import ba.com.zira.praksa.api.FranchiseService;
import ba.com.zira.praksa.api.model.franchise.FranchiseCreateRequest;
import ba.com.zira.praksa.api.model.franchise.FranchiseUpdateRequest;

import ba.com.zira.praksa.api.model.franchise.FranchiseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



@Api(tags = "franchise")
@RestController
@RequestMapping(value = "franchise")
public class FranchiseRestService {

    @Autowired
    private FranchiseService sampleService;

    @ApiOperation(value = "Find Franchises", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<FranchiseResponse> find(@RequestParam(required = false) final String pagination) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);
        return sampleService.find(request);
    }

    @ApiOperation(value = "Get Franchise by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<FranchiseResponse> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return sampleService.findById(request);
    }

    @ApiOperation(value = "Create Franchise", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<FranchiseResponse> createFranchise(@RequestBody EntityRequest<FranchiseCreateRequest> request) throws ApiException {
        return sampleService.create(request);
    }

    @ApiOperation(value = "Update Franchise", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<FranchiseResponse> update(@PathVariable final String id, @RequestBody final EntityRequest<FranchiseUpdateRequest> request)
            throws ApiException {
    	
        final FranchiseUpdateRequest sample = request.getEntity();
        sample.setId(Long.decode(id));

        return sampleService.update(request);
    }

    @ApiOperation(value = "Delete Franchise by Id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        sampleService.delete(request);
    }

}