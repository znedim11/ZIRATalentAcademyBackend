package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.model.concept.Concept;

/**
 * @author irma
 *
 */

public interface ConceptService {

    // Get all Concepts from database
    public PagedPayloadResponse<Concept> find(final SearchRequest<String> request) throws ApiException;

    // Get Concept by Id
    PayloadResponse<Concept> findById(SearchRequest<Long> request) throws ApiException;

    // Create Concept and save it to database
    PayloadResponse<Concept> create(EntityRequest<Concept> request) throws ApiException;

    // Update existing Concept
    PayloadResponse<Concept> update(final EntityRequest<Concept> request) throws ApiException;

    // Delete Concept form database
    PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException;
}
