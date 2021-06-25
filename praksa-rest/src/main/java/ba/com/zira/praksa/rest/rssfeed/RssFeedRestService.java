package ba.com.zira.praksa.rest.rssfeed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.RssFeedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "rss-feed")
@RestController
@RequestMapping(value = "rss-feed")
public class RssFeedRestService {
    @Autowired
    RssFeedService rssFeedService;

    @ApiOperation(value = "Get reviews", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/get-rss")
    public PayloadResponse<String> find(@RequestParam final Long id) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>();
        entityRequest.setEntity(id);
        return rssFeedService.rssFeedReader(entityRequest);
    }
}
