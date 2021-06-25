package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.model.feature.FeatureResponse;
import ba.com.zira.praksa.api.model.game.Game;
import ba.com.zira.praksa.api.model.gamefeature.GameFeatureCreateRequest;
import ba.com.zira.praksa.api.model.gamefeature.GameFeatureResponse;

/**
 * * Methods used to manipulate {@link Game} data. <br>
 * List of APIs implemented in this class with links:
 * <ul>
 * <li>{@link #find}</li>
 * </ul>
 *
 * @author zira
 *
 */
public interface GameService {

    /**
     * Retrieve All {@link Game}s from database.
     *
     * @param request
     *            {@link SearchRequest} containing pagination and sorting
     *            information.
     * @return {@link PagedPayloadResponse} for {@link Game}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PagedPayloadResponse<Game> find(final SearchRequest<String> request) throws ApiException;

    /**
     * Retrieve {@link Game} by Id.
     *
     * @param request
     *            {@link SearchRequest} for Game Id and additional pagination
     *            and sorting information.
     * @return {@link PayloadResponse} for {@link Game}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<Game> findById(SearchRequest<Long> request) throws ApiException;

    /**
     * Create {@link Game}. <br>
     * Method creates Game if the request is valid.
     *
     * @param request
     *            {@link EntityRequest} for {@link Game}
     * @return {@link PayloadResponse} holding created {@link Game}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<Game> create(EntityRequest<Game> request) throws ApiException;

    /**
     * Update existing {@link Game}. <br>
     * Method validates if Game exists and if the request is valid update
     * database.
     *
     * @param request
     *            {@link EntityRequest} for {@link Game}
     * @return {@link PayloadResponse} holding created {@link Game}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<Game> update(final EntityRequest<Game> request) throws ApiException;

    /**
     * Delete {@link Game} from the database. <br>
     * If {@link Game} with the given Id does not exist a validation exception
     * will be thrown.
     *
     * @param request
     *            {@link Game} for Game Id.
     * @return {@link PayloadResponse} confirming deletion.
     * @throws ApiException
     *             If there was a problem during API invocation then
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException;

    /**
     * Retrieve {@link FeatureResponse}s by Games Ids.
     *
     * @param request
     *            {@link SearchRequest} for Games Ids and additional pagination
     *            and sorting information.
     * @return {@link PagedPayloadResponse} for {@link FeatureResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PagedPayloadResponse<FeatureResponse> getFeaturesByGame(final SearchRequest<Long> request) throws ApiException;

    /**
     * Creates {@link GameFeatureResponse} relation. <br>
     *
     * @param request
     *            {@link EntityRequest} for {@link GameFeatureCreateRequest}
     * @return {@link PayloadResponse} holding created
     *         {@link GameFeatureResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<GameFeatureResponse> addFeature(EntityRequest<GameFeatureCreateRequest> request) throws ApiException;

    /**
     * Delete {@link GameFeatureResponse} relation from the database. <br>
     * If {@link GameFeatureResponse} with the given Uuid does not exist a
     * validation exception will be thrown.
     *
     * @param request
     *            {@link String} for GameFeature relation Uuid.
     * @return {@link PayloadResponse} confirming deletion.
     * @throws ApiException
     *             If there was a problem during API invocation then
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<String> removeFeature(EntityRequest<String> request) throws ApiException;
}
