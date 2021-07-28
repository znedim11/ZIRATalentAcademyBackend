package ba.com.zira.praksa.rest.feature;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.FeatureService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.feature.FeatureCreateRequest;
import ba.com.zira.praksa.api.model.feature.FeatureResponse;
import ba.com.zira.praksa.api.model.feature.FeatureUpdateRequest;
import ba.com.zira.praksa.api.model.game.Game;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author zira
 *
 */
@Api(tags = "feature")
@RestController
@RequestMapping(value = "feature")
public class FeatureRestService {

    @Autowired
    FeatureService featureService;

    @ApiOperation(value = "Find Features", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<FeatureResponse> find(@RequestParam(required = false) final String pagination,
            @RequestParam(required = false) final String filter, @RequestParam(required = false) final String sorting) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);
        request.setFilterExpression(filter);
        request.setSorting(sorting);

        return featureService.find(request);
    }

    @ApiOperation(value = "Get Feature by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<FeatureResponse> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return featureService.findById(request);
    }

    @ApiOperation(value = "Create Feature", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<FeatureResponse> createFeature(@RequestBody EntityRequest<FeatureCreateRequest> request) throws ApiException {
        return featureService.create(request);
    }

    @ApiOperation(value = "Update Feature", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<FeatureResponse> update(@PathVariable final Long id,
            @RequestBody final EntityRequest<FeatureUpdateRequest> request) throws ApiException {

        final FeatureUpdateRequest featureUpdateRequest = request.getEntity();
        featureUpdateRequest.setId(id);

        return featureService.update(request);
    }

    @ApiOperation(value = "Delete Feature by Id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{id}")
    public PayloadResponse<String> delete(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return featureService.delete(request);
    }

    @ApiOperation(value = "Get Games by Feature", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/get-games/{id}")
    public PagedPayloadResponse<Game> getGamesByFeature(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        return featureService.getGamesByFeature(request);
    }

    @ApiOperation(value = "Get Set of Games by Set of Features", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/set-of-games")
    public PayloadResponse<Map<String, Set<Game>>> getSetOfGamesByFeature(@RequestParam(required = false) final List<Long> ids)
            throws ApiException {
        ListRequest<Long> request = new ListRequest<>();
        request.setList(ids);

        return featureService.getSetOfGames(request);
    }

    @ApiOperation(value = "Get Feature names by Ids", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/lovs")
    public PagedPayloadResponse<LoV> getLoVs(@RequestParam(required = false) final String pagination,
            @RequestParam(required = false) final String filter) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setPagination(pagination);
        request.setFilterExpression(filter);

        return featureService.getLoVs(request);
    }
}
