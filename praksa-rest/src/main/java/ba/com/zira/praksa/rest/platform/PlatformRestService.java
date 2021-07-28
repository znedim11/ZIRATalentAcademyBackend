package ba.com.zira.praksa.rest.platform;

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
import ba.com.zira.praksa.api.PlatformService;
import ba.com.zira.praksa.api.ReleaseService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.enums.ReleaseType;
import ba.com.zira.praksa.api.model.platform.PlatformCreateRequest;
import ba.com.zira.praksa.api.model.platform.PlatformOverviewResponse;
import ba.com.zira.praksa.api.model.platform.PlatformResponse;
import ba.com.zira.praksa.api.model.platform.PlatformUpdateRequest;
import ba.com.zira.praksa.api.model.release.ReleaseRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "platform")
@RestController
@RequestMapping(value = "platform")
public class PlatformRestService {
    @Autowired
    private PlatformService platformService;
    @Autowired
    private ReleaseService releaseService;

    @ApiOperation(value = "Find Platforms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<PlatformResponse> find(@RequestParam(required = false) final String pagination) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);
        return platformService.find(request);
    }

    @ApiOperation(value = "Get Platform by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<PlatformResponse> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return platformService.findById(request);
    }

    @ApiOperation(value = "Create Platform", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<PlatformResponse> createPlatform(@RequestBody EntityRequest<PlatformCreateRequest> request) throws ApiException {
        return platformService.create(request);
    }

    @ApiOperation(value = "Update Platform", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<PlatformResponse> update(@PathVariable final String id,
            @RequestBody final EntityRequest<PlatformUpdateRequest> request) throws ApiException {

        final PlatformUpdateRequest updateRequest = request.getEntity();
        updateRequest.setId(Long.decode(id));

        return platformService.update(request);
    }

    @ApiOperation(value = "Get Platform names by Ids", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lovs")
    public PagedPayloadResponse<LoV> getLoVs(@RequestParam(required = false) final String pagination,
            @RequestParam(required = false) final String filter) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setPagination(pagination);
        request.setFilterExpression(filter);

        return platformService.getLoVs(request);
    }

    @ApiOperation(value = "Delete Platform by Id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{id}")
    public PayloadResponse<String> delete(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return platformService.delete(request);
    }

    @ApiOperation(value = "Add Release", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/release/add")
    public PayloadResponse<String> addReleasePlatform(@RequestBody EntityRequest<ReleaseRequest> request) throws ApiException {
        final ReleaseRequest addReleaseRequest = request.getEntity();
        addReleaseRequest.setType(ReleaseType.PLATFORM.getValue());
        return releaseService.addRelease(request);
    }

    @ApiOperation(value = "Get Platform Details by Id.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/details/{id}")
    public PayloadResponse<PlatformOverviewResponse> detailsById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return platformService.detail(request);
    }
}
