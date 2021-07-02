package ba.com.zira.praksa.api;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.model.review.CompleteReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewSearchRequest;

/**
 *
 * @author zira
 *
 */

public interface ReviewService {

    public PagedPayloadResponse<ReviewResponse> searchReviews(EntityRequest<ReviewSearchRequest> request) throws ApiException;

    public PayloadResponse<CompleteReviewResponse> getStats(EntityRequest<ReviewSearchRequest> request) throws ApiException;
}
