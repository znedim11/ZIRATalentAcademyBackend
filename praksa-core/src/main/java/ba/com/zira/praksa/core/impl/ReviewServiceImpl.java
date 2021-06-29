package ba.com.zira.praksa.core.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.ReviewService;
import ba.com.zira.praksa.api.model.review.CompleteReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewSearchRequest;
import ba.com.zira.praksa.core.utils.LookupService;
import ba.com.zira.praksa.dao.ReviewDAO;

/**
 * @author zira
 *
 */

@Service
@ComponentScan("ba.com.zira.praksa.core.utils")
public class ReviewServiceImpl implements ReviewService {

    RequestValidator requestValidator;
    ReviewDAO reviewDAO;
    LookupService lookupService;

    public ReviewServiceImpl(RequestValidator requestValidator, ReviewDAO reviewDAO, LookupService lookupService) {
        this.requestValidator = requestValidator;
        this.reviewDAO = reviewDAO;
        this.lookupService = lookupService;
    }

    @Override
    public PagedPayloadResponse<ReviewResponse> searchReviews(EntityRequest<ReviewSearchRequest> request) throws ApiException {
        requestValidator.validate(request);

        List<ReviewResponse> reviewResponseList = reviewDAO.searchReviews(request.getEntity());

        return new PagedPayloadResponse<>(request, ResponseCode.OK, reviewResponseList.size(), 1, 1, reviewResponseList.size(),
                reviewResponseList);
    }

    @Override
    public PayloadResponse<CompleteReviewResponse> getStats(EntityRequest<ReviewSearchRequest> request) throws ApiException {
        requestValidator.validate(request);

        CompleteReviewResponse completeReviewResponse = new CompleteReviewResponse();

        Long totalReviews = Long.valueOf(reviewDAO.searchReviews(request.getEntity()).size());
        completeReviewResponse.setTotalReviews(totalReviews);

        Double sum = 0D;
        for (int i = 0; i < reviewDAO.searchReviews(request.getEntity()).size(); i++) {
            sum += reviewDAO.searchReviews(request.getEntity()).get(i).getTotalRating();
        }
        completeReviewResponse.setAverageGrade(sum / totalReviews);

        if (!reviewDAO.getMostPopularPlatform(request.getEntity()).isEmpty()) {
            completeReviewResponse.setTopPlatformId(reviewDAO.getMostPopularPlatform(request.getEntity()).get(0).getTopPlatformId());
        }

        if (!reviewDAO.getFlopGame(request.getEntity()).isEmpty()) {
            completeReviewResponse.setFlopGameId(reviewDAO.getFlopGame(request.getEntity()).get(0).getFlopGameId());
        }

        if (!reviewDAO.getTopGame(request.getEntity()).isEmpty()) {
            completeReviewResponse.setTopGameId(reviewDAO.getTopGame(request.getEntity()).get(0).getTopGameId());
        }

        lookupService.lookupPlatformName(Arrays.asList(completeReviewResponse), CompleteReviewResponse::getTopPlatformId,
                CompleteReviewResponse::setTopPlatformName);

        lookupService.lookupGameName(Arrays.asList(completeReviewResponse), CompleteReviewResponse::getTopGameId,
                CompleteReviewResponse::setFlopGameName);

        lookupService.lookupGameName(Arrays.asList(completeReviewResponse), CompleteReviewResponse::getTopGameId,
                CompleteReviewResponse::setTopGameName);

        return new PayloadResponse<>(request, ResponseCode.OK, completeReviewResponse);
    }

}
