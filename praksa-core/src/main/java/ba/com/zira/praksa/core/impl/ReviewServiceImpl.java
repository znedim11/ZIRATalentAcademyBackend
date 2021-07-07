package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

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
import ba.com.zira.praksa.api.model.review.ReviewUpdateRequest;
import ba.com.zira.praksa.core.utils.LookupService;
import ba.com.zira.praksa.core.validation.ReviewRequestValidation;
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
    static final String VALIDATE_ABSTRACT_REQUEST = "validateAbstractRequest";
    static final String BASIC_NOT_NULL = "basicNotNull";
    static final String TOTAL_GRADE_TYPE = "TOTAL_GRADE";

    RequestValidator requestValidator;
    ReviewRequestValidation reviewRequestValidation;
    ReviewDAO reviewDAO;
    LookupService lookupService;
    ReviewMapper reviewMapper;
    GameDAO gameDAO;
    FormulaDAO formulaDAO;
    ReviewGradeDAO reviewGradeDAO;

    public ReviewServiceImpl(RequestValidator requestValidator, ReviewRequestValidation reviewRequestValidation, ReviewDAO reviewDAO,
            LookupService lookupService, ReviewMapper reviewMapper, GameDAO gameDAO, FormulaDAO formulaDAO, ReviewGradeDAO reviewGradeDAO) {
        super();
        this.requestValidator = requestValidator;
        this.reviewRequestValidation = reviewRequestValidation;
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
        requestValidator.validate(request);
        reviewRequestValidation.validateEntityExists(request, BASIC_NOT_NULL);
        reviewRequestValidation.validateRequiredFieldsExistsInCreateRequest(request, BASIC_NOT_NULL);

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
        totalGradeEntity.setType(TOTAL_GRADE_TYPE);
        totalGradeEntity.setUuid(UUID.randomUUID().toString());

        reviewGradeDAO.merge(totalGradeEntity);

        if (request.getEntity().getGrades() != null) {
            for (Map.Entry<String, Double> grade : request.getEntity().getGrades().entrySet()) {
                ReviewGradeEntity gradeEntity = new ReviewGradeEntity();
                gradeEntity.setFormulaId(request.getEntity().getFormulaId());
                gradeEntity.setGrade(grade.getValue());
                gradeEntity.setReview(createdReviewEntity);
                gradeEntity.setType(grade.getKey());
                gradeEntity.setUuid(UUID.randomUUID().toString());

                reviewGradeDAO.merge(gradeEntity);
            }
        }

        ReviewResponse response = reviewMapper.reviewEntityToReview(reviewEntity);
        response.setTotalRating(totalGradeEntity.getGrade());

        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    public PayloadResponse<ReviewResponse> update(EntityRequest<ReviewUpdateRequest> request) throws ApiException {
        reviewRequestValidation.validateEntityExists(request, BASIC_NOT_NULL);

        EntityRequest<Long> entityRequestId = new EntityRequest<>(request.getEntity().getId(), request);
        reviewRequestValidation.validateReviewExists(entityRequestId, VALIDATE_ABSTRACT_REQUEST);

        reviewRequestValidation.validateRequiredFieldsExistsInUpdateRequest(request, BASIC_NOT_NULL);

        final ReviewUpdateRequest reviewRequest = request.getEntity();

        ReviewEntity reviewEntity = reviewDAO.findByPK(request.getEntity().getId());
        reviewEntity = reviewMapper.updateRequestToEntity(reviewRequest, reviewEntity);
        reviewEntity.setReviewFormula(formulaDAO.findByPK(request.getEntity().getFormulaId()));
        reviewEntity.setModified(LocalDateTime.now());
        reviewEntity.setModifiedBy(request.getUserId());

        reviewDAO.merge(reviewEntity);

        List<ReviewGradeEntity> gradeEntities = reviewGradeDAO.getGradesByReview(reviewEntity.getId()).stream()
                .collect(Collectors.toList());

        ReviewGradeEntity totalGradeEntity = gradeEntities.stream().filter(g -> g.getType().equals(TOTAL_GRADE_TYPE))
                .collect(Collectors.toList()).get(0);
        totalGradeEntity.setGrade(request.getEntity().getTotalRating());
        reviewGradeDAO.merge(totalGradeEntity);

        Map<String, Double> currentGrades = gradeEntities.stream().filter(g -> !g.getType().equals(TOTAL_GRADE_TYPE))
                .collect(Collectors.toMap(ReviewGradeEntity::getType, ReviewGradeEntity::getGrade));

        if (request.getEntity().getGrades() != null) {
            final Map<String, Double> gradesToDelete = Maps.difference(currentGrades, request.getEntity().getGrades()).entriesOnlyOnLeft();
            final Map<String, Double> gradesToAdd = Maps.difference(request.getEntity().getGrades(), currentGrades).entriesOnlyOnLeft();
            final Map<String, MapDifference.ValueDifference<Double>> gradesToEdit = Maps
                    .difference(currentGrades, request.getEntity().getGrades()).entriesDiffering();

            for (Map.Entry<String, MapDifference.ValueDifference<Double>> grade : gradesToEdit.entrySet()) {
                ReviewGradeEntity gradeEntity = gradeEntities.stream().filter(g -> g.getType().equals(grade.getKey()))
                        .collect(Collectors.toList()).get(0);
                gradeEntity.setGrade(grade.getValue().rightValue());

                reviewGradeDAO.merge(gradeEntity);
            }

            for (Map.Entry<String, Double> grade : gradesToDelete.entrySet()) {
                ReviewGradeEntity gradeEntity = gradeEntities.stream().filter(g -> g.getType().equals(grade.getKey()))
                        .collect(Collectors.toList()).get(0);

                reviewGradeDAO.remove(gradeEntity);
            }

            for (Map.Entry<String, Double> grade : gradesToAdd.entrySet()) {
                ReviewGradeEntity gradeEntity = new ReviewGradeEntity();
                gradeEntity.setFormulaId(request.getEntity().getFormulaId());
                gradeEntity.setGrade(grade.getValue());
                gradeEntity.setReview(reviewEntity);
                gradeEntity.setType(grade.getKey());
                gradeEntity.setUuid(UUID.randomUUID().toString());

                reviewGradeDAO.merge(gradeEntity);
            }
        }

        final ReviewResponse response = reviewMapper.reviewEntityToReview(reviewEntity);
        response.setTotalRating(totalGradeEntity.getGrade());

        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

}
