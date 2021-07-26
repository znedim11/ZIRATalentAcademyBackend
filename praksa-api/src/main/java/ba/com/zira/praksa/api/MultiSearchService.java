package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.praksa.api.model.MultiSearchResponse;

public interface MultiSearchService {

    PagedPayloadResponse<MultiSearchResponse> findByName(SearchRequest<String> request) throws ApiException;

}
