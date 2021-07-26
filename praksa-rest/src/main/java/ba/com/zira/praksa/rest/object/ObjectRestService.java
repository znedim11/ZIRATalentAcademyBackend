package ba.com.zira.praksa.rest.object;

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
import ba.com.zira.praksa.api.ObjectService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.object.ObjectCreateRequest;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.api.model.object.ObjectUpdateRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author zira
 *
 */
@Api(tags = "object")
@RestController
@RequestMapping(value = "object")
public class ObjectRestService {

    @Autowired
    ObjectService objectService;

    @ApiOperation(value = "Find objects", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<ObjectResponse> find(@RequestParam(required = false) final String pagination) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);

        return objectService.find(request);
    }

    @ApiOperation(value = "Get object by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<ObjectResponse> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return objectService.findById(request);
    }

    @ApiOperation(value = "Create object", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<ObjectResponse> createObject(@RequestBody EntityRequest<ObjectCreateRequest> request) throws ApiException {
        return objectService.create(request);
    }

    @ApiOperation(value = "Update object", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<ObjectResponse> update(@PathVariable final Long id,
            @RequestBody final EntityRequest<ObjectUpdateRequest> request) throws ApiException {

        final ObjectUpdateRequest sample = request.getEntity();
        sample.setId(id);

        return objectService.update(request);
    }

    @ApiOperation(value = "Delete object by Id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        objectService.delete(request);
    }

    @ApiOperation(value = "Get Object names by Ids.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lovs")
    public ListPayloadResponse<LoV> getLoVs(@RequestParam(required = false) final List<Long> ids) throws ApiException {

        final ListRequest<Long> request = new ListRequest<>();
        request.setList(ids);

        return objectService.getLoVs(request);
    }

    @ApiOperation(value = "Get Objects not Connected to ...", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lov-not-connected")
    public ListPayloadResponse<LoV> getLoVNotConnectedTo(@RequestParam(required = true) final String type,
            @RequestParam(required = true) final Long id) throws ApiException {

        final EntityRequest<LoV> request = new EntityRequest<>();
        request.setEntity(new LoV(id, type));

        return objectService.getLoVsNotConnectedTo(request);
    }
}
