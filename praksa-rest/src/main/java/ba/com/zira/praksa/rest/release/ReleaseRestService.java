package ba.com.zira.praksa.rest.release;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import ba.com.zira.commons.model.User;
import ba.com.zira.praksa.api.ReleaseService;
import ba.com.zira.praksa.api.model.release.ReleaseRequest;
import ba.com.zira.praksa.api.model.release.ReleaseResponse;
import ba.com.zira.praksa.api.model.release.ReleaseResponseLight;
import ba.com.zira.praksa.api.model.release.ReleasesByTimetableRequest;
import ba.com.zira.praksa.api.model.release.ReleasesByTimetableResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "release")
@RestController
@RequestMapping(value = "release")
public class ReleaseRestService {

    @Autowired
    private ReleaseService releaseService;

    @ApiOperation(value = "Find Release", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/find")
    public PagedPayloadResponse<ReleaseResponse> find(@RequestParam(required = false) final String pagination,
            @RequestParam(required = false) final String sorting, @RequestParam(required = false) final String filter) throws ApiException {

        SearchRequest<String> request = new SearchRequest<>();
        request.setPagination(pagination);
        request.setFilterExpression(filter);
        request.setSorting(sorting);
        return releaseService.find(request);
    }

    @ApiOperation(value = "Find Release By Uuid", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{uuid}")
    public PayloadResponse<ReleaseResponseLight> findByUuid(@PathVariable final String uuid) throws ApiException {

        final EntityRequest<String> request = new EntityRequest<>();
        request.setEntity(uuid);
        return releaseService.findByUuid(request);
    }

    @ApiOperation(value = "Add Release", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/add")
    public PayloadResponse<String> addRelease(@RequestBody final EntityRequest<ReleaseRequest> request) throws ApiException {
        return releaseService.addRelease(request);
    }

    @ApiOperation(value = "Delete Release by Uuid", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/{uuid}")
    public void delete(@PathVariable final String uuid) throws ApiException {
        final EntityRequest<String> request = new EntityRequest<>();
        request.setEntity(uuid);

        releaseService.delete(request);
    }

    @ApiOperation(value = "Get Releases by Timetable", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/releases-by-timetable")
    public PayloadResponse<ReleasesByTimetableResponse> getReleasesByTimetable(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate endDate,
            @RequestParam(required = false) final String timeSegment, final String releaseType) throws ApiException {

        final EntityRequest<ReleasesByTimetableRequest> request = new EntityRequest<>();
        ReleasesByTimetableRequest releasesByTimetableRequest = new ReleasesByTimetableRequest();

        releasesByTimetableRequest.setStartDate(startDate != null ? startDate.atStartOfDay() : null);
        releasesByTimetableRequest.setEndDate(endDate != null ? endDate.atStartOfDay() : null);
        releasesByTimetableRequest.setTimeSegment(timeSegment);
        releasesByTimetableRequest.setReleaseType(releaseType);

        request.setEntity(releasesByTimetableRequest);
        User user = new User("System");
        request.setUser(user);
        return releaseService.getReleasesByTimetable(request);
    }
}
