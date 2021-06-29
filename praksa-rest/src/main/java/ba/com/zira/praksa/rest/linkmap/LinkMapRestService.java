package ba.com.zira.praksa.rest.linkmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.LinkMapService;
import ba.com.zira.praksa.api.model.linkmap.LinkMapResponse;
import ba.com.zira.praksa.api.model.linkmap.LinkRequest;
import ba.com.zira.praksa.api.model.linkmap.MultipleLinkRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "link")
@RestController
@RequestMapping(value = "link")
public class LinkMapRestService {
    @Autowired
    LinkMapService linkMapService;

    @ApiOperation(value = "Find LinkMaps", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<LinkMapResponse> find(@RequestParam(required = false) final String pagination) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);
        return linkMapService.find(request);
    }

    @ApiOperation(value = "Get LinkMap by uuid.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{Uuid}")
    public PayloadResponse<LinkMapResponse> findByUuid(@PathVariable final String Uuid) throws ApiException {

        final EntityRequest<String> request = new EntityRequest<>();
        request.setEntity(Uuid);

        return linkMapService.findByUuid(request);
    }

    @ApiOperation(value = "Delete LinkMap by Uuid", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{Uuid}")
    public void delete(@PathVariable final String Uuid) throws ApiException {
        final EntityRequest<String> request = new EntityRequest<>();
        request.setEntity(Uuid);

        linkMapService.delete(request);
    }

    @ApiOperation(value = "Create single Link", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/single")
    public PayloadResponse<String> single(@RequestBody EntityRequest<LinkRequest> request) throws ApiException {
        return linkMapService.createSingleLinkRequest(request);
    }

    @ApiOperation(value = "Create multiple Link", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/multiple")
    public PayloadResponse<String> multiple(@RequestBody EntityRequest<MultipleLinkRequest> request) throws ApiException {
        return linkMapService.createMultipleLinkRequest(request);
    }
}
