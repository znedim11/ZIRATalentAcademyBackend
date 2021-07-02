package ba.com.zira.praksa.rest.media;

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
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.FileUploadService;
import ba.com.zira.praksa.api.MediaService;
import ba.com.zira.praksa.api.model.media.CreateMediaRequest;
import ba.com.zira.praksa.api.model.media.Media;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "media")
@RestController
@RequestMapping(value = "media")

public class MediaRestService {
    @Autowired
    private MediaService mediaService;

    @Autowired
    private FileUploadService fileUploadService;

    @ApiOperation(value = "Find Media", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<Media> find(@RequestParam(required = false) final String pagination) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);
        return mediaService.find(request);
    }

    @ApiOperation(value = "Get Media by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<Media> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return mediaService.findById(request);
    }

    @ApiOperation(value = "Create Media", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<Media> createMedia(@RequestBody EntityRequest<Media> request) throws ApiException {
        return mediaService.create(request);
    }

    @ApiOperation(value = "Update Media", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<Media> update(@PathVariable final String id, @RequestBody final EntityRequest<Media> request)
            throws ApiException {

        final Media sample = request.getEntity();
        sample.setId(Long.decode(id));

        return mediaService.update(request);
    }

    @ApiOperation(value = "Delete Media by Id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable final Long id) throws ApiException {
        final EntityRequest<Long> request = new EntityRequest<>();
        request.setEntity(id);

        mediaService.delete(request);
    }

    @ApiOperation(value = "Upload Image", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/upload")
    public PayloadResponse<String> saveMedia(@RequestBody EntityRequest<CreateMediaRequest> imageUploadRequest) throws ApiException {
        return mediaService.saveMedia(imageUploadRequest);
    }
}
