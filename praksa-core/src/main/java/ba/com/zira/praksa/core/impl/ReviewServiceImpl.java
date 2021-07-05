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
import ba.com.zira.praksa.dao.ExternalReviewDAO;
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
    ExternalReviewDAO externalReviewDAO;
    LookupService lookupService;

    public ReviewServiceImpl(RequestValidator requestValidator, ReviewDAO reviewDAO, LookupService lookupService,
            ExternalReviewDAO externalReviewDAO) {
        this.requestValidator = requestValidator;
        this.reviewDAO = reviewDAO;
        this.lookupService = lookupService;
        this.externalReviewDAO = externalReviewDAO;
    }

    @Override
    public PagedPayloadResponse<ReviewResponse> searchReviews(EntityRequest<ReviewSearchRequest> request) throws ApiException {
        requestValidator.validate(request);

        ReviewSearchRequest searchRequest = request.getEntity();
        String reviewType = searchRequest.getType();

        if (reviewType == null) {
            reviewType = "both";
        }

        List<ReviewResponse> reviewResponseList = null;

        if (reviewType.equalsIgnoreCase("internal")) {
            reviewResponseList = reviewDAO.searchReviews(request.getEntity());
        } else if (reviewType.equalsIgnoreCase("external")) {
            reviewResponseList = externalReviewDAO.searchReviews(request.getEntity());
        } else if (reviewType.equalsIgnoreCase("both")) {
            reviewResponseList = reviewDAO.searchReviews(searchRequest);
            List<ReviewResponse> externalReviewsList = externalReviewDAO.searchReviews(searchRequest);

            reviewResponseList.addAll(externalReviewsList);
        }

        lookupService.lookupReviewerName(reviewResponseList, ReviewResponse::getReviewerId, ReviewResponse::setReviewerName);

        return new PagedPayloadResponse<>(request, ResponseCode.OK, reviewResponseList.size(), 1, 1, reviewResponseList.size(),
                reviewResponseList);
    }

    @Override
    public PayloadResponse<CompleteReviewResponse> getStats(EntityRequest<ReviewSearchRequest> request) throws ApiException {
        requestValidator.validate(request);

        CompleteReviewResponse completeReviewResponse = new CompleteReviewResponse();

        Long totalReviews = Long.valueOf(this.searchReviews(request).getPayload().size());
        completeReviewResponse.setTotalReviews(totalReviews);

        String reviewType = request.getEntity().getType();
        if (reviewType == null) {
            reviewType = "both";
        }

        if (reviewType.equalsIgnoreCase("internal")) {
            Double sum = 0D;
            for (int i = 0; i < reviewDAO.searchReviews(request.getEntity()).size(); i++) {
                sum += Double.valueOf(reviewDAO.searchReviews(request.getEntity()).get(i).getTotalRating());
            }
            completeReviewResponse.setAverageGrade(sum / reviewDAO.searchReviews(request.getEntity()).size());

            if (!reviewDAO.getMostPopularPlatform(request.getEntity()).isEmpty()) {
                completeReviewResponse.setTopPlatformId(reviewDAO.getMostPopularPlatform(request.getEntity()).get(0).getTopPlatformId());
            } else {
                completeReviewResponse.setTopPlatformId(null);
            }

            if (!reviewDAO.getFlopGame(request.getEntity()).isEmpty()) {
                completeReviewResponse.setFlopGameId(reviewDAO.getFlopGame(request.getEntity()).get(0).getFlopGameId());
            } else {
                completeReviewResponse.setFlopGameId(null);
            }

            if (!reviewDAO.getTopGame(request.getEntity()).isEmpty()) {
                completeReviewResponse.setTopGameId(reviewDAO.getTopGame(request.getEntity()).get(0).getTopGameId());
            } else {
                completeReviewResponse.setTopGameId(null);
            }

        } else if (reviewType.equalsIgnoreCase("external")) {

            if (!reviewDAO.getMostPopularPlatform(request.getEntity()).isEmpty()) {
                completeReviewResponse
                        .setTopPlatformId(externalReviewDAO.getMostPopularPlatform(request.getEntity()).get(0).getTopPlatformId());
            } else {
                completeReviewResponse.setTopPlatformId(null);
            }

        } else {

            Double sum = 0D;
            Long totalSize = 0L;
            List<ReviewResponse> searchReviewResponse = this.searchReviews(request).getPayload();
            for (int i = 0; i < searchReviewResponse.size(); i++) {
                if (searchReviewResponse.get(i).getType().equalsIgnoreCase("internal")) {
                    sum += Double.valueOf(searchReviewResponse.get(i).getTotalRating());
                    totalSize++;
                }
            }
            completeReviewResponse.setAverageGrade(sum / totalSize);
            if (!reviewDAO.getTopGame(request.getEntity()).isEmpty()) {
                completeReviewResponse.setTopGameId(reviewDAO.getTopGame(request.getEntity()).get(0).getTopGameId());
            } else {
                completeReviewResponse.setTopGameId(null);
            }
            if (!reviewDAO.getFlopGame(request.getEntity()).isEmpty()) {
                completeReviewResponse.setFlopGameId(reviewDAO.getFlopGame(request.getEntity()).get(0).getFlopGameId());
            } else {
                completeReviewResponse.setFlopGameId(null);
            }

            if (!reviewDAO.getMostPopularPlatform(request.getEntity()).isEmpty()
                    && !externalReviewDAO.getMostPopularPlatform(request.getEntity()).isEmpty()) {
                if (reviewDAO.getMostPopularPlatform(request.getEntity()).get(0).getTotalReviews() > externalReviewDAO
                        .getMostPopularPlatform(request.getEntity()).get(0).getTotalReviews()) {
                    completeReviewResponse
                            .setTopPlatformId(reviewDAO.getMostPopularPlatform(request.getEntity()).get(0).getTopPlatformId());
                } else {
                    completeReviewResponse
                            .setTopPlatformId(externalReviewDAO.getMostPopularPlatform(request.getEntity()).get(0).getTopPlatformId());
                }

            } else if (reviewDAO.getMostPopularPlatform(request.getEntity()).isEmpty()
                    && !externalReviewDAO.getMostPopularPlatform(request.getEntity()).isEmpty()) {
                completeReviewResponse
                        .setTopPlatformId(externalReviewDAO.getMostPopularPlatform(request.getEntity()).get(0).getTopPlatformId());
            } else if (!reviewDAO.getMostPopularPlatform(request.getEntity()).isEmpty()
                    && externalReviewDAO.getMostPopularPlatform(request.getEntity()).isEmpty()) {
                completeReviewResponse.setTopPlatformId(reviewDAO.getMostPopularPlatform(request.getEntity()).get(0).getTopPlatformId());
            }

        }

        lookupService.lookupGameName(Arrays.asList(completeReviewResponse), CompleteReviewResponse::getFlopGameId,
                CompleteReviewResponse::setFlopGameName);

        lookupService.lookupGameName(Arrays.asList(completeReviewResponse), CompleteReviewResponse::getTopGameId,
                CompleteReviewResponse::setTopGameName);

        lookupService.lookupPlatformName(Arrays.asList(completeReviewResponse), CompleteReviewResponse::getTopPlatformId,
                CompleteReviewResponse::setTopPlatformName);

        return new PayloadResponse<>(request, ResponseCode.OK, completeReviewResponse);
    }

}
