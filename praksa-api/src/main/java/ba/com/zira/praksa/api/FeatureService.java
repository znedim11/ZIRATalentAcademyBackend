package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.model.feature.Feature;
import ba.com.zira.praksa.api.model.feature.FeatureCreateRequest;

/**
 * * Methods used to manipulate {@link Feature} data.
 * 
 * @author Ajas
 *
 */
public interface FeatureService {

    // Gets/Returns all Features that satisfy passed request
    public PagedPayloadResponse<Feature> find(final SearchRequest<String> request) throws ApiException;

    // Returns Feature with the corresponding id
    PayloadResponse<Feature> findById(SearchRequest<Long> request) throws ApiException;

    // Method creates Feature if the request is valid.
    PayloadResponse<Feature> create(EntityRequest<FeatureCreateRequest> request) throws ApiException;

    // Method validates if Feature exists and if the request is valid for update
    // action
    PayloadResponse<Feature> update(final EntityRequest<Feature> request) throws ApiException;

    // Deletes Feature with corresponding Id, If Feature with the given Id does
    // not exist a validation
    // exception will be thrown
    PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException;

}
