package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.model.externalreview.ExternalReview;
import ba.com.zira.praksa.api.model.externalreview.ExternalReviewCreateRequest;
import ba.com.zira.praksa.api.model.externalreview.ExternalReviewUpdateRequest;
import ba.com.zira.praksa.api.model.game.Game;

/**
 * * Methods used to manipulate {@link ExternalReview} data. * Methods used to
 * manipulate ExternalReview data. <br>
 * List of APIs implemented in this class with links:
 * <ul>
 * <li>{@link #find}</li>
 * <li>{@link #findById}</li>
 * <li>{@link #create}</li>
 * <li>{@link #update}</li>
 * <li>{@link #delete}</li>
 * </ul>
 *
 * @author zira
 *
 */

public interface ExternalReviewService {
    /**
     * Retrieve All {@link ExternalReview}s from database.
     *
     * @param request
     *            {@link SearchRequest} containing pagination and sorting
     *            information.
     * @return {@link PagedPayloadResponse} for {@link ExternalReview}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    public PagedPayloadResponse<ExternalReview> find(final SearchRequest<String> request) throws ApiException;

    /**
     * Retrieve {@link ExternalReview} by Id.
     *
     * @param request
     *            {@link SearchRequest} for Sample Id and additional pagination
     *            and sorting information.
     * @return {@link PayloadResponse} for {@link ExternalReview}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<ExternalReview> findById(SearchRequest<Long> request) throws ApiException;

    /**
     * Create {@link ExternalReview}. <br>
     * Method creates Sample if the request is valid.
     *
     * @param request
     *            {@link EntityRequest} for {@link ExternalReview}
     * @return {@link PayloadResponse} holding created {@link ExternalReview}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<ExternalReview> create(EntityRequest<ExternalReviewCreateRequest> request) throws ApiException;

    /**
     * Update existing {@link Game}. <br>
     * Method validates if Sample exists and if the request is valid update
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
    PayloadResponse<ExternalReview> update(final EntityRequest<ExternalReviewUpdateRequest> request) throws ApiException;

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

}
