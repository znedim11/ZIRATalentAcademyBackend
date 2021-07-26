package ba.com.zira.praksa.rest.location;

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
import ba.com.zira.praksa.api.LocationService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.location.Location;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "location")
@RestController
@RequestMapping(value = "location")
public class LocationRestService {

    @Autowired
    private LocationService locationService;

    @ApiOperation(value = "Create Location", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<Location> createLocation(@RequestBody EntityRequest<Location> request) throws ApiException {
        return locationService.create(request);
    }

    @ApiOperation(value = "Delete Location by Id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        locationService.delete(request);
    }

    @ApiOperation(value = "Find Locations", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<Location> find(@RequestParam(required = false) final String pagination) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);
        return locationService.find(request);
    }

    @ApiOperation(value = "Get Location by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<Location> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return locationService.findById(request);
    }

    @ApiOperation(value = "Update Location", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<Location> update(@PathVariable final String id, @RequestBody final EntityRequest<Location> request)
            throws ApiException {

        final Location sample = request.getEntity();
        sample.setId(Long.decode(id));

        return locationService.update(request);
    }

    @ApiOperation(value = "Get Location names by Ids.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lovs")
    public ListPayloadResponse<LoV> getLoVs(@RequestParam(required = false) final List<Long> ids) throws ApiException {

        final ListRequest<Long> request = new ListRequest<>();
        request.setList(ids);

        return locationService.getLoVs(request);
    }

    @ApiOperation(value = "Get Locations not Connected to ...", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lov-not-connected")
    public ListPayloadResponse<LoV> getLoVNotConnectedTo(@RequestParam(required = true) final String type,
            @RequestParam(required = true) final Long id) throws ApiException {

        final EntityRequest<LoV> request = new EntityRequest<>();
        request.setEntity(new LoV(id, type));

        return locationService.getLoVsNotConnectedTo(request);
    }
}
