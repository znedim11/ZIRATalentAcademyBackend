package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.company.CompanyCreateRequest;
import ba.com.zira.praksa.api.model.company.CompanyResponse;
import ba.com.zira.praksa.api.model.company.CompanyUpdateRequest;

public interface CompanyService {

    public PagedPayloadResponse<CompanyResponse> find(final SearchRequest<String> request) throws ApiException;

    PayloadResponse<CompanyResponse> findById(SearchRequest<Long> request) throws ApiException;

    PayloadResponse<CompanyResponse> create(EntityRequest<CompanyCreateRequest> request) throws ApiException;

    PayloadResponse<CompanyResponse> update(final EntityRequest<CompanyUpdateRequest> request) throws ApiException;

    PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException;

    public ListPayloadResponse<LoV> lovs(ListRequest<Long> request) throws ApiException;
}