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
import ba.com.zira.praksa.api.model.enums.ReviewType;
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
        List<ReviewResponse> reviewResponse = reviewDAO.searchReviews(searchRequest);
        List<ReviewResponse> externalReviewResponse = externalReviewDAO.searchReviews(searchRequest);
        String reviewType = searchRequest.getType();
        List<ReviewResponse> reviewResponseList = null;

        if (reviewType == null) {
            reviewType = ReviewType.BOTH.getValue();
        }

        if (reviewType.equalsIgnoreCase(ReviewType.INTERNAL.getValue())) {
            reviewResponseList = reviewResponse;
        } else if (reviewType.equalsIgnoreCase(ReviewType.EXTERNAL.getValue())) {
            reviewResponseList = externalReviewResponse;
        } else if (reviewType.equalsIgnoreCase(ReviewType.BOTH.getValue())) {
            reviewResponseList = reviewResponse;
            reviewResponseList.addAll(externalReviewResponse);
        }

        lookupService.lookupReviewerName(reviewResponseList, ReviewResponse::getReviewerId, ReviewResponse::setReviewerName);

        return new PagedPayloadResponse<>(request, ResponseCode.OK, reviewResponseList.size(), 1, 1, reviewResponseList.size(),
                reviewResponseList);
    }

    @Override
    public PayloadResponse<CompleteReviewResponse> getStats(final EntityRequest<ReviewSearchRequest> request) throws ApiException {
        requestValidator.validate(request);

        CompleteReviewResponse completeReviewResponse = new CompleteReviewResponse();
        ReviewSearchRequest searchRequest = request.getEntity();
        List<ReviewResponse> reviewResponse = reviewDAO.searchReviews(searchRequest);
        Long reviewResponseSize = Long.valueOf(reviewResponse.size());
        List<ReviewResponse> searchReviewResponse = searchReviews(request).getPayload();
        Long totalReviews = Long.valueOf(searchReviewResponse.size());
        String reviewType = searchRequest.getType();
        Double sum = 0D;

        boolean isPlatformsEmpty = reviewDAO.getMostPopularPlatform(searchRequest).isEmpty();
        boolean isTopGamesEmpty = reviewDAO.getTopGame(searchRequest).isEmpty();
        boolean isFlopGamesEmpty = reviewDAO.getFlopGame(searchRequest).isEmpty();
        Long mostPopularPlatformId = reviewDAO.getMostPopularPlatform(searchRequest).get(0).getTopPlatformId();
        Long topGameId = reviewDAO.getTopGame(searchRequest).get(0).getTopGameId();
        Long flopGameId = reviewDAO.getFlopGame(searchRequest).get(0).getFlopGameId();

        completeReviewResponse.setTotalReviews(totalReviews);

        if (reviewType == null) {
            reviewType = ReviewType.BOTH.getValue();
        }

        if (reviewType.equalsIgnoreCase(ReviewType.INTERNAL.getValue())) {
            for (int i = 0; i < reviewResponseSize; i++) {
                sum += Double.valueOf(reviewResponse.get(i).getTotalRating());
            }
            completeReviewResponse.setAverageGrade(sum / reviewResponseSize);
            setTopPlatform(completeReviewResponse, isPlatformsEmpty, mostPopularPlatformId);
            setTopGame(completeReviewResponse, isTopGamesEmpty, topGameId);
            setFlopGame(completeReviewResponse, isFlopGamesEmpty, flopGameId);

        } else if (reviewType.equalsIgnoreCase(ReviewType.EXTERNAL.getValue())) {
            setTopPlatform(completeReviewResponse, isPlatformsEmpty, mostPopularPlatformId);
        } else {
            Long totalSize = 0L;
            for (int i = 0; i < totalReviews; i++) {
                if (searchReviewResponse.get(i).getType().equalsIgnoreCase(ReviewType.INTERNAL.getValue())) {
                    sum += Double.valueOf(searchReviewResponse.get(i).getTotalRating());
                    totalSize++;
                }
            }
            if (totalSize > 0) {
                completeReviewResponse.setAverageGrade(sum / totalSize);
            }

            setTopPlatform(completeReviewResponse, isPlatformsEmpty, mostPopularPlatformId, searchRequest);
            setTopGame(completeReviewResponse, isTopGamesEmpty, topGameId);
            setFlopGame(completeReviewResponse, isFlopGamesEmpty, flopGameId);
        }

        lookupService.lookupGameName(Arrays.asList(completeReviewResponse), CompleteReviewResponse::getFlopGameId,
                CompleteReviewResponse::setFlopGameName);

        lookupService.lookupGameName(Arrays.asList(completeReviewResponse), CompleteReviewResponse::getTopGameId,
                CompleteReviewResponse::setTopGameName);

        lookupService.lookupPlatformName(Arrays.asList(completeReviewResponse), CompleteReviewResponse::getTopPlatformId,
                CompleteReviewResponse::setTopPlatformName);

        return new PayloadResponse<>(request, ResponseCode.OK, completeReviewResponse);
    }

    private void setTopPlatform(CompleteReviewResponse completeReviewResponse, boolean isPlatformsEmpty, Long mostPopularPlatformId,
            ReviewSearchRequest searchRequest) {
        boolean isExternalPlatformsEmpty = externalReviewDAO.getMostPopularPlatform(searchRequest).isEmpty();
        Long mostPopularExternalPlatformId = externalReviewDAO.getMostPopularPlatform(searchRequest).get(0).getTopPlatformId();
        Long totalIntReviews = reviewDAO.getMostPopularPlatform(searchRequest).get(0).getTotalReviews();
        Long totalExtReviews = externalReviewDAO.getMostPopularPlatform(searchRequest).get(0).getTotalReviews();

        if (!isPlatformsEmpty && !isExternalPlatformsEmpty) {
            completeReviewResponse
                    .setTopPlatformId(totalIntReviews > totalExtReviews ? mostPopularPlatformId : mostPopularExternalPlatformId);
        } else if (isPlatformsEmpty && !isExternalPlatformsEmpty) {
            completeReviewResponse.setTopPlatformId(mostPopularExternalPlatformId);
        } else if (!isPlatformsEmpty && isExternalPlatformsEmpty) {
            completeReviewResponse.setTopPlatformId(mostPopularPlatformId);
        }
    }

    private void setFlopGame(CompleteReviewResponse completeReviewResponse, boolean isFlopGamesEmpty, Long flopGameId) {
        completeReviewResponse.setFlopGameId(!isFlopGamesEmpty ? flopGameId : null);
    }

    private void setTopGame(CompleteReviewResponse completeReviewResponse, boolean isTopGamesEmpty, Long topGameId) {
        completeReviewResponse.setTopGameId(!isTopGamesEmpty ? topGameId : null);
    }

    private void setTopPlatform(CompleteReviewResponse completeReviewResponse, boolean isPlatformsEmpty, Long mostPopularPlatformId) {
        completeReviewResponse.setTopPlatformId(!isPlatformsEmpty ? mostPopularPlatformId : null);
    }
}
