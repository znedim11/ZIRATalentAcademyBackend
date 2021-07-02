package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.model.release.ReleaseRequest;
import ba.com.zira.praksa.api.model.release.ReleasesByTimetableRequest;
import ba.com.zira.praksa.api.model.release.ReleasesByTimetableResponse;

public interface ReleaseService {
    public PayloadResponse<String> addRelease(final EntityRequest<ReleaseRequest> request) throws ApiException;

    public PayloadResponse<ReleasesByTimetableResponse> getReleasesByTimetable(final EntityRequest<ReleasesByTimetableRequest> request)
            throws ApiException;

}
