package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.model.linkmap.LinkMapResponse;
import ba.com.zira.praksa.api.model.linkmap.LinkRequest;
import ba.com.zira.praksa.api.model.linkmap.MultipleLinkRequest;

/**
 *
 * @author zira
 *
 */

public interface LinkMapService {
    public PagedPayloadResponse<LinkMapResponse> find(final SearchRequest<String> request) throws ApiException;

    PayloadResponse<LinkMapResponse> findByUuid(EntityRequest<String> request) throws ApiException;

    PayloadResponse<String> delete(EntityRequest<String> request) throws ApiException;

    PayloadResponse<String> createSingleLinkRequest(EntityRequest<LinkRequest> request) throws ApiException;

    PayloadResponse<String> createMultipleLinkRequest(EntityRequest<MultipleLinkRequest> request) throws ApiException;
}
