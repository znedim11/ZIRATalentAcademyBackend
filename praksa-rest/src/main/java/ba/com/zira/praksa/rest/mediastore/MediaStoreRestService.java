package ba.com.zira.praksa.rest.mediastore;

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
import ba.com.zira.praksa.api.MediaStoreService;
import ba.com.zira.praksa.api.model.mediastore.MediaStoreCreateRequest;
import ba.com.zira.praksa.api.model.mediastore.MediaStoreResponse;
import ba.com.zira.praksa.api.model.mediastore.MediaStoreUpdateRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "mediastore")
@RestController
@RequestMapping(value = "mediastore")
public class MediaStoreRestService {

    @Autowired
    private MediaStoreService mediaStoreService;

    @ApiOperation(value = "Find MediaStore", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<MediaStoreResponse> find(@RequestParam(required = false) final String pagination) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);
        return mediaStoreService.find(request);
    }

    @ApiOperation(value = "Get MediaStore by uuid.", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{Uuid}")
    public PayloadResponse<MediaStoreResponse> findByUuid(@PathVariable final String Uuid) throws ApiException {

        final EntityRequest<String> request = new EntityRequest<>();
        request.setEntity(Uuid);

        return mediaStoreService.findByUuid(request);
    }

    @ApiOperation(value = "Create MediaStore", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<MediaStoreResponse> createMediaStore(@RequestBody EntityRequest<MediaStoreCreateRequest> request)
            throws ApiException {
        return mediaStoreService.create(request);
    }

    @ApiOperation(value = "Update MediaStore", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{Uuid}")
    public PayloadResponse<MediaStoreResponse> update(@PathVariable final String Uuid,
            @RequestBody final EntityRequest<MediaStoreUpdateRequest> request) throws ApiException {

        final MediaStoreUpdateRequest sample = request.getEntity();
        sample.setUuid(String.format(Uuid));

        return mediaStoreService.update(request);
    }

    @ApiOperation(value = "Delete MediaStore by Uuid", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{Uuid}")
    public void delete(@PathVariable final String Uuid) throws ApiException {
        final EntityRequest<String> request = new EntityRequest<>();
        request.setEntity(Uuid);

        mediaStoreService.delete(request);
    }

}
