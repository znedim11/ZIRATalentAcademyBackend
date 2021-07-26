package ba.com.zira.praksa.rest.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.praksa.api.MultiSearchService;
import ba.com.zira.praksa.api.model.MultiSearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "rss-feed")
@RestController
@RequestMapping(value = "search-all")
public class SearchRestService {
    @Autowired
    MultiSearchService multiSearchService;

    @ApiOperation(value = "Get by name", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/name")
    public PagedPayloadResponse<MultiSearchResponse> findByName(@RequestParam final String searchTerm,
            @RequestParam final String pagination) throws ApiException {
        SearchRequest<String> request = new SearchRequest<>();
        request.setEntity(searchTerm);
        request.setPagination(pagination);
        return multiSearchService.findByName(request);
    }
}
