package ba.com.zira.praksa.rest.release;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.ReleaseService;
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

    @ApiOperation(value = "Get Releases by Timetable", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/releases-by-timetable")
    public PayloadResponse<ReleasesByTimetableResponse> getReleasesByTimetable(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate endDate,
            @RequestParam(required = false) final String timeSegment) throws ApiException {

        final EntityRequest<ReleasesByTimetableRequest> request = new EntityRequest<>();
        ReleasesByTimetableRequest releasesByTimetableRequest = new ReleasesByTimetableRequest();

        releasesByTimetableRequest.setStartDate(startDate != null ? startDate.atStartOfDay() : null);
        releasesByTimetableRequest.setEndDate(endDate != null ? endDate.atStartOfDay() : null);
        releasesByTimetableRequest.setTimeSegment(timeSegment);

        request.setEntity(releasesByTimetableRequest);

        return releaseService.getReleasesByTimetable(request);
    }
}
