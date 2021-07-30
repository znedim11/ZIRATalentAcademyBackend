package ba.com.zira.praksa.rest.franchise;

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
import ba.com.zira.praksa.api.FranchiseService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.franchise.FranchiseCreateRequest;
import ba.com.zira.praksa.api.model.franchise.FranchiseOverviewResponse;
import ba.com.zira.praksa.api.model.franchise.FranchiseResponse;
import ba.com.zira.praksa.api.model.franchise.FranchiseUpdateRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "franchise")
@RestController
@RequestMapping(value = "franchise")
public class FranchiseRestService {

    @Autowired
    private FranchiseService franchiseService;

    @ApiOperation(value = "Find Franchises", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<FranchiseResponse> find(@RequestParam(required = false) final String pagination) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);
        return franchiseService.find(request);
    }

    @ApiOperation(value = "Get Franchise by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<FranchiseResponse> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return franchiseService.findById(request);
    }

    @ApiOperation(value = "Create Franchise", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<FranchiseResponse> createFranchise(@RequestBody EntityRequest<FranchiseCreateRequest> request)
            throws ApiException {
        return franchiseService.create(request);
    }

    @ApiOperation(value = "Update Franchise", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<FranchiseResponse> update(@PathVariable final String id,
            @RequestBody final EntityRequest<FranchiseUpdateRequest> request) throws ApiException {

        final FranchiseUpdateRequest sample = request.getEntity();
        sample.setId(Long.decode(id));

        return franchiseService.update(request);
    }

    @ApiOperation(value = "Get Franchise names by Ids", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lovs")
    public ListPayloadResponse<LoV> getLoVs(@RequestParam(required = false) final List<Long> ids) throws ApiException {
        final ListRequest<Long> request = new ListRequest<>();
        request.setList(ids);
        return franchiseService.getLoVs(request);
    }

    @ApiOperation(value = "Delete Franchise by Id", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{id}")
    public PayloadResponse<String> delete(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return franchiseService.delete(request);
    }

    @ApiOperation(value = "Get Franchise Information by Id", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/information/{id}")
    public PayloadResponse<FranchiseOverviewResponse> getInformationById(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);
        return franchiseService.getInformationById(request);
    }

}