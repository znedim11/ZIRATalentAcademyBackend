package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.ReviewService;
import ba.com.zira.praksa.api.model.review.CompleteReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewCreateRequest;
import ba.com.zira.praksa.api.model.review.ReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewSearchRequest;
import ba.com.zira.praksa.core.utils.LookupService;
import ba.com.zira.praksa.dao.FormulaDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.ReviewDAO;
import ba.com.zira.praksa.dao.ReviewGradeDAO;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.dao.model.ReviewEntity;
import ba.com.zira.praksa.dao.model.ReviewFormulaEntity;
import ba.com.zira.praksa.dao.model.ReviewGradeEntity;
import ba.com.zira.praksa.mapper.ReviewMapper;

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
    ReviewMapper reviewMapper;
    GameDAO gameDAO;
    FormulaDAO formulaDAO;
    ReviewGradeDAO reviewGradeDAO;

    public ReviewServiceImpl(RequestValidator requestValidator, ReviewDAO reviewDAO, LookupService lookupService, ReviewMapper reviewMapper,
            GameDAO gameDAO, FormulaDAO formulaDAO, ReviewGradeDAO reviewGradeDAO) {
        super();
        this.requestValidator = requestValidator;
        this.reviewDAO = reviewDAO;
        this.lookupService = lookupService;
        this.reviewMapper = reviewMapper;
        this.gameDAO = gameDAO;
        this.formulaDAO = formulaDAO;
        this.reviewGradeDAO = reviewGradeDAO;
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

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<ReviewResponse> create(EntityRequest<ReviewCreateRequest> request) throws ApiException {
        // TODO: Dodati custom validaciju
        requestValidator.validate(request);

        ReviewEntity reviewEntity = reviewMapper.createRequestToEntity(request.getEntity());
        GameEntity gameEntity = gameDAO.findByPK(request.getEntity().getGameId());
        ReviewFormulaEntity reviewFormulaEntity = formulaDAO.findByPK(request.getEntity().getFormulaId());

        reviewEntity.setCreated(LocalDateTime.now());
        reviewEntity.setCreatedBy(request.getUserId());
        reviewEntity.setGame(gameEntity);
        reviewEntity.setReviewFormula(reviewFormulaEntity);

        ReviewEntity createdReviewEntity = reviewDAO.persist(reviewEntity);

        ReviewGradeEntity totalGradeEntity = new ReviewGradeEntity();
        totalGradeEntity.setFormulaId(request.getEntity().getFormulaId());
        totalGradeEntity.setGrade(request.getEntity().getTotalRating());
        totalGradeEntity.setReview(createdReviewEntity);
        totalGradeEntity.setType("TOTAL_GRADE");
        totalGradeEntity.setUuid(UUID.randomUUID().toString());

        reviewGradeDAO.merge(totalGradeEntity);

        if (request.getEntity().getGrades() != null) {
            for (Map.Entry<String, Double> grade : request.getEntity().getGrades().entrySet()) {
                ReviewGradeEntity gradeEntity = new ReviewGradeEntity();
                totalGradeEntity.setFormulaId(request.getEntity().getFormulaId());
                totalGradeEntity.setGrade(grade.getValue());
                totalGradeEntity.setReview(createdReviewEntity);
                totalGradeEntity.setType(grade.getKey());
                totalGradeEntity.setUuid(UUID.randomUUID().toString());

                reviewGradeDAO.merge(gradeEntity);
            }
        }

        ReviewResponse response = reviewMapper.reviewEntityToReview(reviewEntity);
        response.setFormulaId(createdReviewEntity.getReviewFormula().getId());
        response.setGameId(createdReviewEntity.getGame().getId());

        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

}
