package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.model.linkMap.LinkRequest;
import ba.com.zira.praksa.api.model.linkMap.MultipleLinkRequest;

/**
 *
 * @author zira
 *
 */

public interface LinkMapService {

    PayloadResponse<String> createSingleLinkRequest(EntityRequest<LinkRequest> request) throws ApiException;

    PayloadResponse<String> createMultipleLinkRequest(EntityRequest<MultipleLinkRequest> request) throws ApiException;
}
