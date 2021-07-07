package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.model.review.CompleteReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewCreateRequest;
import ba.com.zira.praksa.api.model.review.ReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewSearchRequest;
import ba.com.zira.praksa.api.model.review.ReviewUpdateRequest;

/**
 *
 * @author zira
 *
 */

public interface ReviewService {

    public PagedPayloadResponse<ReviewResponse> searchReviews(EntityRequest<ReviewSearchRequest> request) throws ApiException;

    public PayloadResponse<CompleteReviewResponse> getStats(EntityRequest<ReviewSearchRequest> request) throws ApiException;

    /**
     * Create {@link ReviewResponse}. <br>
     * Method creates Sample if the request is valid.
     *
     * @param request
     *            {@link EntityRequest} for {@link ReviewResponse}
     * @return {@link PayloadResponse} holding created {@link ReviewResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<ReviewResponse> create(EntityRequest<ReviewCreateRequest> request) throws ApiException;

    /**
     * Update existing {@link ReviewResponse}. <br>
     * Method validates if Sample exists and if the request is valid update
     * database.
     *
     * @param request
     *            {@link EntityRequest} for {@link ReviewResponse}
     * @return {@link PayloadResponse} holding created {@link ReviewResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<ReviewResponse> update(final EntityRequest<ReviewUpdateRequest> request) throws ApiException;

    /**
     * Retrieve {@link ReviewResponse} by Id.
     *
     * @param request
     *            {@link SearchRequest} for ReviewResponse Id and additional
     *            pagination and sorting information.
     * @return {@link PayloadResponse} for {@link ReviewResponse}.
     * @throws ApiException
     *             If there was a problem during API invocation then.
     *             {@link ApiException} will be generated/returned with
     *             corresponding error message and {@link ResponseCode}.
     */
    PayloadResponse<ReviewResponse> findById(SearchRequest<Long> request) throws ApiException;

    // ListPayloadResponse<ReviewResponse> getGradesByReview(SearchRequest<Long>
    // request) throws ApiException;

}
