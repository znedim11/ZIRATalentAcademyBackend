package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.model.concept.ConceptResponse;
import ba.com.zira.praksa.api.model.concept.ConceptRequest;

/**
 * @author irma
 *
 */

public interface ConceptService {

    // Get all Concepts from database
    public PagedPayloadResponse<ConceptResponse> find(final SearchRequest<String> request) throws ApiException;

    // Get Concept by Id
    PayloadResponse<ConceptResponse> findById(SearchRequest<Long> request) throws ApiException;

    // Create Concept and save it to database
    PayloadResponse<ConceptResponse> create(EntityRequest<ConceptRequest> request) throws ApiException;

    // Update existing Concept
    PayloadResponse<ConceptResponse> update(final EntityRequest<ConceptRequest> request) throws ApiException;

    // Delete Concept form database
    PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException;
}
