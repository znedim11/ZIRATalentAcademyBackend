package ba.com.zira.praksa.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.Request;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.SampleService;
import ba.com.zira.praksa.api.request.SampleRequest;
import ba.com.zira.praksa.api.response.SampleResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "sample")
@RestController
@RequestMapping(value = "sample")
public class SampleRestService {

    @Autowired
    private SampleService sampleService;

    @ApiOperation(value = "Find Samples", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<SampleResponse> find(@RequestParam(required = false) final String pagination) throws ApiException {

        Request request = new Request();
        request.setPagination(pagination);
        return sampleService.find(request);
    }
    
    @ApiOperation(value = "Get Sample by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<SampleResponse> findById(@PathVariable final String id) throws ApiException {

        final SearchRequest<String> request = new SearchRequest<>();
        request.setEntity(id);

        return sampleService.findById(request);
    }
    @ApiOperation(value = "Create Sample", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<SampleResponse> createSample(@RequestBody EntityRequest<SampleRequest> request) throws ApiException {
        return sampleService.createSample(request);
    }
    
    @ApiOperation(value = "Update Sample", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public PayloadResponse<SampleResponse> update(@PathVariable final String id, @RequestBody final EntityRequest<SampleRequest> request)
            throws ApiException {

        final SampleRequest sample = request.getEntity();
        sample.setId(Long.decode(id));

        return sampleService.update(request);
    }
    
    @ApiOperation(value = "Delete Sample by Id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable final String id) throws ApiException {
        final EntityRequest<String> request = new EntityRequest<>();
        request.setEntity(id);

        sampleService.delete(request);
    }

}
