package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.franchise.FranchiseCreateRequest;
import ba.com.zira.praksa.api.model.franchise.FranchiseOverviewResponse;
import ba.com.zira.praksa.api.model.franchise.FranchiseResponse;
import ba.com.zira.praksa.api.model.franchise.FranchiseUpdateRequest;

public interface FranchiseService {
    public PagedPayloadResponse<FranchiseResponse> find(final SearchRequest<String> request) throws ApiException;

    PayloadResponse<FranchiseResponse> findById(SearchRequest<Long> request) throws ApiException;

    PayloadResponse<FranchiseResponse> create(EntityRequest<FranchiseCreateRequest> request) throws ApiException;

    PayloadResponse<FranchiseResponse> update(final EntityRequest<FranchiseUpdateRequest> request) throws ApiException;

    PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException;

    ListPayloadResponse<LoV> getLoVs(final ListRequest<Long> request) throws ApiException;

    public PayloadResponse<FranchiseOverviewResponse> getInformationById(EntityRequest<Long> request) throws ApiException;

}
