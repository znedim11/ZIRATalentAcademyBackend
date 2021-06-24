package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.model.release.ReleaseRequest;

public interface ReleaseService
{
	public PayloadResponse<String> addRelease(final EntityRequest<ReleaseRequest> request) throws ApiException;

}
