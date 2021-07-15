package ba.com.zira.praksa.api;

import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.praksa.api.model.LoV;

public interface RegionService {

    ListPayloadResponse<LoV> lovs(final ListRequest<Long> ids);
}
