package ba.com.zira.praksa.rest.region;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.praksa.api.RegionService;
import ba.com.zira.praksa.api.model.LoV;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "region")
@RestController
@RequestMapping(value = "region")
public class RegionRestService {

    @Autowired
    RegionService regionService;

    @ApiOperation(value = "Find Regions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lovs")
    public ListPayloadResponse<LoV> lovs(@RequestParam(required = false) List<Long> ids) {

        ListRequest<Long> request = new ListRequest<>();
        request.setList(ids);
        return regionService.lovs(request);
    }
}
