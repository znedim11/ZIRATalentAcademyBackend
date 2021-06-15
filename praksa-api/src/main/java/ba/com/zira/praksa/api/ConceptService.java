package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.model.concept.ConceptCreateRequest;
import ba.com.zira.praksa.api.model.concept.ConceptResponse;
import ba.com.zira.praksa.api.model.concept.ConceptUpdateRequest;
import ba.com.zira.praksa.api.model.game.Game;

/**
 * * Methods used to manipulate {@link ConceptResponse} data. <br>
 * List of APIs implemented in this class with links:
 * <ul>
 * <li>{@link #find}</li>
 * </ul>
 *
 * @author zira
 *
 */

public interface ConceptService {
    /**
     * Retrieve All {@link ConceptResponse}s from database.
     *
     * @param request
     *            {@link SearchRequest} containing pagination and sorting
     *            information.
     * @return {@link PagedPayloadResponse} for {@link ConceptResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PagedPayloadResponse<ConceptResponse> find(final SearchRequest<String> request) throws ApiException;

    /**
     * Retrieve {@link ConceptResponse} by Id.
     *
     * @param request
     *            {@link SearchRequest} for ConceptResponse Id and additional
     *            pagination and sorting information.
     * @return {@link PayloadResponse} for {@link ConceptResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<ConceptResponse> findById(SearchRequest<Long> request) throws ApiException;

    /**
     * Create {@link ConceptResponse}. <br>
     * Method creates Sample if the request is valid.
     *
     * @param request
     *            {@link EntityRequest} for {@link ConceptResponse}
     * @return {@link PayloadResponse} holding created {@link ConceptResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<ConceptResponse> create(EntityRequest<ConceptCreateRequest> request) throws ApiException;

    /**
     * Update existing {@link ConceptResponse}. <br>
     * Method validates if Sample exists and if the request is valid update
     * database.
     *
     * @param request
     *            {@link EntityRequest} for {@link ConceptResponse}
     * @return {@link PayloadResponse} holding created {@link ConceptResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<ConceptResponse> update(final EntityRequest<ConceptUpdateRequest> request) throws ApiException;

    /**
     * Delete {@link ConceptResponse} from the database. <br>
     * If {@link ConceptResponse} with the given Id does not exist a validation
     * exception will be thrown.
     *
     * @param request
     *            {@link ConceptResponse} for ConceptResponse Id.
     * @return {@link PayloadResponse} confirming deletion.
     * @throws ApiException
     *             If there was a problem during API invocation then
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException;

    // ListPayloadResponse<Game> getGamesByConcept(SearchRequest<Long> request)
    // throws ApiException;

    PagedPayloadResponse<Game> getGamesByConcept(final EntityRequest<Long> request) throws ApiException;

}
