package ba.com.zira.praksa.rest.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.MediaService;
import ba.com.zira.praksa.api.model.media.Media;
import io.swagger.annotations.ApiOperation;

public class MediaRestService {
    @Autowired
    private MediaService sampleService;

    @ApiOperation(value = "Find Media", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<Media> find(@RequestParam(required = false) final String pagination) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);
        return sampleService.find(request);
    }

    @ApiOperation(value = "Get Game by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<Game> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return sampleService.findById(request);
    }

    @ApiOperation(value = "Create Game", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<Game> createGame(@RequestBody EntityRequest<Game> request) throws ApiException {
        return sampleService.create(request);
    }

    @ApiOperation(value = "Update Game", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<Game> update(@PathVariable final String id, @RequestBody final EntityRequest<Game> request) throws ApiException {

        final Game sample = request.getEntity();
        sample.setId(Long.decode(id));

        return sampleService.update(request);
    }

    @ApiOperation(value = "Delete Game by Id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        sampleService.delete(request);
    }
}
