package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.platform.PlatformCreateRequest;
import ba.com.zira.praksa.api.model.platform.PlatformOverviewResponse;
import ba.com.zira.praksa.api.model.platform.PlatformResponse;
import ba.com.zira.praksa.api.model.platform.PlatformUpdateRequest;

public interface PlatformService {
    public PagedPayloadResponse<PlatformResponse> find(final SearchRequest<String> request) throws ApiException;

    public PayloadResponse<PlatformResponse> findById(SearchRequest<Long> request) throws ApiException;

    public PayloadResponse<PlatformResponse> create(EntityRequest<PlatformCreateRequest> request) throws ApiException;

    public PagedPayloadResponse<LoV> getLoVs(final SearchRequest<Long> request) throws ApiException;

    public PayloadResponse<PlatformResponse> update(final EntityRequest<PlatformUpdateRequest> request) throws ApiException;

    public PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException;

    PayloadResponse<PlatformOverviewResponse> detail(SearchRequest<Long> request) throws ApiException;
}
