package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EmptyRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.praksa.api.model.MultiSearchResponse;
import ba.com.zira.praksa.api.model.WikiStatsResponse;

public interface MultiSearchService {

    PagedPayloadResponse<MultiSearchResponse> findByName(SearchRequest<String> request) throws ApiException;

    ListPayloadResponse<WikiStatsResponse> findWikiStats(EmptyRequest request);

}
