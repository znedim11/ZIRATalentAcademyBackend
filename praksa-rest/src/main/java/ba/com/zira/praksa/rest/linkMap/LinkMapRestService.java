package ba.com.zira.praksa.rest.linkMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.LinkMapService;
import ba.com.zira.praksa.api.model.linkMap.LinkRequest;
import ba.com.zira.praksa.api.model.linkMap.MultipleLinkRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "linkMap")
@RestController
@RequestMapping(value = "linkMap")
public class LinkMapRestService {
    @Autowired
    private LinkMapService linkMapService;

    @ApiOperation(value = "Create single Link", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/single")
    public PayloadResponse<String> single(@RequestBody EntityRequest<LinkRequest> request) throws ApiException {
        return linkMapService.single(request);
    }

    @ApiOperation(value = "Create multiple Link", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/multiple")
    public PayloadResponse<String> multiple(@RequestBody EntityRequest<MultipleLinkRequest> request) throws ApiException {
        return linkMapService.multiple(request);
    }
}
