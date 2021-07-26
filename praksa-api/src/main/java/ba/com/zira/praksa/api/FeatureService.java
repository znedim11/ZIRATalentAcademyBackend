package ba.com.zira.praksa.api;

import java.util.Map;
import java.util.Set;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.feature.FeatureCreateRequest;
import ba.com.zira.praksa.api.model.feature.FeatureResponse;
import ba.com.zira.praksa.api.model.feature.FeatureUpdateRequest;
import ba.com.zira.praksa.api.model.game.Game;

/**
 * * Methods used to manipulate Feature data. <br>
 * List of APIs implemented in this class with links:
 * <ul>
 * <li>{@link #find}</li>
 * <li>{@link #findById}</li>
 * <li>{@link #create}</li>
 * <li>{@link #update}</li>
 * <li>{@link #delete}</li>
 * <li>{@link #getGamesByFeature}</li>
 * <li>{@link #getSetOfGames}</li>
 * </ul>
 *
 * @author zira
 *
 */
public interface FeatureService {

    /**
     * Retrieve All {@link FeatureResponse}s from database.
     *
     * @param request
     *            {@link SearchRequest} containing pagination and sorting
     *            information.
     * @return {@link PagedPayloadResponse} for {@link FeatureResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PagedPayloadResponse<FeatureResponse> find(final SearchRequest<String> request) throws ApiException;

    /**
     * Retrieve {@link FeatureResponse} by Id.
     *
     * @param request
     *            {@link SearchRequest} for Feature Id and additional pagination
     *            and sorting information.
     * @return {@link PayloadResponse} for {@link FeatureResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<FeatureResponse> findById(SearchRequest<Long> request) throws ApiException;

    /**
     * Create Feature. <br>
     * Method creates FeatureEntity if the request is valid.
     *
     * @param request
     *            {@link EntityRequest} for {@link FeatureCreateRequest}
     * @return {@link PayloadResponse} holding created {@link FeatureResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<FeatureResponse> create(EntityRequest<FeatureCreateRequest> request) throws ApiException;

    /**
     * Update existing Feature. <br>
     * Method validates if FeatureEntity exists and if the request is valid to
     * update database.
     *
     * @param request
     *            {@link EntityRequest} for {@link FeatureUpdateRequest}
     * @return {@link PayloadResponse} holding created {@link FeatureResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<FeatureResponse> update(final EntityRequest<FeatureUpdateRequest> request) throws ApiException;

    /**
     * Delete Feature from the database. <br>
     * If Feature with the given Id does not exist a validation exception will
     * be thrown.
     *
     * @param request
     *            {@link Long} for Feature Id.
     * @return {@link PayloadResponse} confirming deletion.
     * @throws ApiException
     *             If there was a problem during API invocation then
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException;

    /**
     * Retrieve list of {@link Game}s from database by Feature Id.
     *
     * @param request
     *            {@link EntityRequest} containing Feature id.
     * @return {@link PagedPayloadResponse} for {@link Game}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PagedPayloadResponse<Game> getGamesByFeature(final EntityRequest<Long> request) throws ApiException;

    /**
     * Retrieve entry sets of {@link Game}s where key sets are all combinations
     * of passed Features.
     *
     * @param request
     *            {@link ListRequest} containing Feature ids.
     * @return {@link PayloadResponse} for Games Map.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<Map<String, Set<Game>>> getSetOfGames(ListRequest<Long> request) throws ApiException;

    public PagedPayloadResponse<LoV> getLoVs(SearchRequest<Long> request) throws ApiException;
}
