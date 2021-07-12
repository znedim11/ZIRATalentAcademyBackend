package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.ReviewService;
import ba.com.zira.praksa.api.model.enums.ReviewType;
import ba.com.zira.praksa.api.model.review.CompleteReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewCreateRequest;
import ba.com.zira.praksa.api.model.review.ReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewSearchRequest;
import ba.com.zira.praksa.api.model.review.ReviewUpdateRequest;
import ba.com.zira.praksa.api.model.reviewgrade.ReviewGradeCreateRequest;
import ba.com.zira.praksa.api.model.reviewgrade.ReviewGradeResponse;
import ba.com.zira.praksa.core.utils.LookupService;
import ba.com.zira.praksa.core.validation.ReviewRequestValidation;
import ba.com.zira.praksa.dao.ExternalReviewDAO;
import ba.com.zira.praksa.dao.FormulaDAO;
import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.dao.ReviewDAO;
import ba.com.zira.praksa.dao.ReviewGradeDAO;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.dao.model.ReviewEntity;
import ba.com.zira.praksa.dao.model.ReviewFormulaEntity;
import ba.com.zira.praksa.dao.model.ReviewGradeEntity;
import ba.com.zira.praksa.mapper.ReviewGradeMapper;
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
    ExternalReviewDAO externalReviewDAO;
    LookupService lookupService;
    ReviewMapper reviewMapper;
    GameDAO gameDAO;
    FormulaDAO formulaDAO;
    ReviewGradeDAO reviewGradeDAO;
    ReviewGradeMapper reviewGradeMapper;

    public ReviewServiceImpl(RequestValidator requestValidator, ReviewRequestValidation reviewRequestValidation, ReviewDAO reviewDAO,
            ExternalReviewDAO externalReviewDAO, LookupService lookupService, ReviewMapper reviewMapper, GameDAO gameDAO,
            FormulaDAO formulaDAO, ReviewGradeDAO reviewGradeDAO, ReviewGradeMapper reviewGradeMapper) {
        super();
        this.requestValidator = requestValidator;
        this.reviewRequestValidation = reviewRequestValidation;
        this.reviewDAO = reviewDAO;
        this.externalReviewDAO = externalReviewDAO;
        this.lookupService = lookupService;
        this.reviewMapper = reviewMapper;
        this.gameDAO = gameDAO;
        this.formulaDAO = formulaDAO;
        this.reviewGradeDAO = reviewGradeDAO;
        this.reviewGradeMapper = reviewGradeMapper;
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

        if (reviewResponse == null) {
            reviewResponse = new ArrayList<>();
        }

        if (reviewType.equalsIgnoreCase(ReviewType.INTERNAL.getValue())) {
            reviewResponseList = reviewResponse;
        } else if (reviewType.equalsIgnoreCase(ReviewType.EXTERNAL.getValue())) {
            reviewResponseList = externalReviewResponse;
        } else {
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
        reviewRequestValidation.validateEntityExists(request, BASIC_NOT_NULL);

        CompleteReviewResponse completeReviewResponse = new CompleteReviewResponse();
        ReviewSearchRequest searchRequest = request.getEntity();
        List<ReviewResponse> reviewResponse = reviewDAO.searchReviews(searchRequest);
        Long reviewResponseSize = Long.valueOf(reviewResponse.size());
        List<ReviewResponse> searchReviewResponse = searchReviews(request).getPayload();
        Long totalReviews = Long.valueOf(searchReviewResponse.size());
        String reviewType = searchRequest.getType();
        Double sum = 0D;

        boolean isPlatformsEmpty = reviewDAO.getMostPopularPlatform(searchRequest).isEmpty();
        Long mostPopularPlatformId = !isPlatformsEmpty ? reviewDAO.getMostPopularPlatform(searchRequest).get(0).getTopPlatformId() : null;
        boolean isTopGamesEmpty = reviewDAO.getTopGame(searchRequest).isEmpty();
        Long topGameId = !isTopGamesEmpty ? reviewDAO.getTopGame(searchRequest).get(0).getTopGameId() : null;
        boolean isFlopGamesEmpty = reviewDAO.getFlopGame(searchRequest).isEmpty();
        Long flopGameId = !isFlopGamesEmpty ? reviewDAO.getFlopGame(searchRequest).get(0).getFlopGameId() : null;
        boolean isExternalPlatformsEmpty = externalReviewDAO.getMostPopularPlatform(searchRequest).isEmpty();
        Long mostPopularExternalPlatformId = !isExternalPlatformsEmpty
                ? externalReviewDAO.getMostPopularPlatform(searchRequest).get(0).getTopPlatformId()
                : null;

        completeReviewResponse.setTotalReviews(totalReviews);

        if (reviewType == null) {
            reviewType = ReviewType.BOTH.getValue();
        }

        if (reviewType.equalsIgnoreCase(ReviewType.INTERNAL.getValue())) {
            for (int i = 0; i < reviewResponseSize; i++) {
                sum += reviewResponse.get(i).getTotalRating();
            }
            completeReviewResponse.setAverageGrade(sum / reviewResponseSize);

            setTopPlatform(completeReviewResponse, isPlatformsEmpty, mostPopularPlatformId);
            setTopGame(completeReviewResponse, isTopGamesEmpty, topGameId);
            setFlopGame(completeReviewResponse, isFlopGamesEmpty, flopGameId);

        } else if (reviewType.equalsIgnoreCase(ReviewType.EXTERNAL.getValue())) {
            setTopPlatform(completeReviewResponse, isExternalPlatformsEmpty, mostPopularExternalPlatformId);
        } else if (reviewType.equalsIgnoreCase(ReviewType.BOTH.getValue())) {
            Long totalSize = 0L;
            for (int i = 0; i < totalReviews; i++) {
                if (searchReviewResponse.get(i).getType().equalsIgnoreCase(ReviewType.INTERNAL.getValue())) {
                    sum += searchReviewResponse.get(i).getTotalRating();
                    totalSize++;
                }
            }
            if (totalSize > 0) {
                completeReviewResponse.setAverageGrade(sum / totalSize);
            }

            setTopPlatform(mostPopularExternalPlatformId, isExternalPlatformsEmpty, completeReviewResponse, isPlatformsEmpty,
                    mostPopularPlatformId, searchRequest);
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

    private void setTopPlatform(Long mostPopularExternalPlatformId, boolean isExternalPlatformsEmpty,
            CompleteReviewResponse completeReviewResponse, boolean isPlatformsEmpty, Long mostPopularPlatformId,
            ReviewSearchRequest searchRequest) {
        Long totalIntReviews = !isPlatformsEmpty ? reviewDAO.getMostPopularPlatform(searchRequest).get(0).getTotalReviews() : 0;
        Long totalExtReviews = !isExternalPlatformsEmpty ? externalReviewDAO.getMostPopularPlatform(searchRequest).get(0).getTotalReviews()
                : 0;

        if (!isPlatformsEmpty && !isExternalPlatformsEmpty) {
            completeReviewResponse
                    .setTopPlatformId(totalIntReviews > totalExtReviews ? mostPopularPlatformId : mostPopularExternalPlatformId);
        } else if (isPlatformsEmpty && !isExternalPlatformsEmpty) {
            completeReviewResponse.setTopPlatformId(mostPopularExternalPlatformId);
        } else if (!isPlatformsEmpty && isExternalPlatformsEmpty) {
            completeReviewResponse.setTopPlatformId(mostPopularPlatformId);
        }
    }

    private void setTopGame(CompleteReviewResponse completeReviewResponse, boolean isTopGamesEmpty, Long topGameId) {
        completeReviewResponse.setTopGameId(!isTopGamesEmpty ? topGameId : null);
    }

    private void setTopPlatform(CompleteReviewResponse completeReviewResponse, boolean isPlatformsEmpty, Long mostPopularPlatformId) {
        completeReviewResponse.setTopPlatformId(!isPlatformsEmpty ? mostPopularPlatformId : null);
    }

    private void setFlopGame(CompleteReviewResponse completeReviewResponse, boolean isFlopGamesEmpty, Long flopGameId) {
        completeReviewResponse.setFlopGameId(!isFlopGamesEmpty ? flopGameId : null);
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
            for (ReviewGradeCreateRequest grade : request.getEntity().getGrades()) {
                ReviewGradeEntity gradeEntity = reviewGradeMapper.reviewGradeCreateRequestToEntity(grade);
                gradeEntity.setFormulaId(request.getEntity().getFormulaId());
                gradeEntity.setReview(createdReviewEntity);
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

        List<ReviewGradeEntity> currentGradeEntites = gradeEntities.stream().filter(g -> !g.getType().equals(TOTAL_GRADE_TYPE))
                .collect(Collectors.toList());

        List<ReviewGradeResponse> requestGrades = reviewGradeMapper
                .reviewGradeCreateRequestListToResponseList(request.getEntity().getGrades());
        List<ReviewGradeResponse> currentGrades = reviewGradeMapper.reviewGradeEntityListToResponseList(currentGradeEntites);

        if (request.getEntity().getGrades() != null) {
            final List<ReviewGradeResponse> gradesToDelete = new ArrayList<>(
                    Sets.difference(new HashSet<>(currentGrades), new HashSet<>(requestGrades)));
            final List<ReviewGradeResponse> gradesToAdd = new ArrayList<>(
                    Sets.difference(new HashSet<>(requestGrades), new HashSet<>(currentGrades)));
            final List<ReviewGradeResponse> gradesToEdit = new ArrayList<>(
                    Sets.intersection(new HashSet<>(requestGrades), new HashSet<>(currentGrades)));

            for (ReviewGradeResponse grade : gradesToEdit) {
                ReviewGradeEntity gradeEntity = currentGradeEntites.stream().filter(g -> g.getType().equals(grade.getType()))
                        .collect(Collectors.toList()).get(0);
                gradeEntity.setGrade(grade.getGrade());

                reviewGradeDAO.merge(gradeEntity);
            }

            for (ReviewGradeResponse grade : gradesToDelete) {
                ReviewGradeEntity gradeEntity = gradeEntities.stream().filter(g -> g.getType().equals(grade.getType()))
                        .collect(Collectors.toList()).get(0);

                reviewGradeDAO.remove(gradeEntity);
            }

            for (ReviewGradeResponse grade : gradesToAdd) {
                ReviewGradeEntity gradeEntity = reviewGradeMapper.reviewGradeResponseToEntity(grade);
                gradeEntity.setFormulaId(request.getEntity().getFormulaId());
                gradeEntity.setReview(reviewEntity);
                gradeEntity.setUuid(UUID.randomUUID().toString());

                reviewGradeDAO.merge(gradeEntity);
            }
        }

        final ReviewResponse response = reviewMapper.reviewEntityToReview(reviewEntity);
        response.setTotalRating(totalGradeEntity.getGrade());

        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    public PayloadResponse<ReviewResponse> findById(SearchRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        reviewRequestValidation.validateReviewExists(entityRequest, VALIDATE_ABSTRACT_REQUEST);

        final ReviewEntity reviewEntity = reviewDAO.findByPK(request.getEntity());

        final ReviewResponse reviewResponse = reviewMapper.reviewEntityToReview(reviewEntity);

        List<ReviewGradeEntity> gradeEntities = reviewGradeDAO.getGradesByReview(reviewEntity.getId()).stream()
                .collect(Collectors.toList());

        ReviewGradeEntity totalGradeEntity = gradeEntities.stream().filter(g -> g.getType().equals(TOTAL_GRADE_TYPE))
                .collect(Collectors.toList()).get(0);

        List<ReviewGradeEntity> grades = gradeEntities.stream().filter(g -> !g.getType().equals(TOTAL_GRADE_TYPE))
                .collect(Collectors.toList());
        List<ReviewGradeResponse> gradeResponses = reviewGradeMapper.reviewGradeEntityListToResponseList(grades);

        reviewResponse.setGrades(gradeResponses);
        reviewResponse.setTotalRating(totalGradeEntity.getGrade());
        reviewResponse.setReviewerId(reviewEntity.getCreatedBy());
        reviewResponse.setNumOfReviewesByReviewer(reviewDAO.getNumberOfReviewsByReviewer(reviewEntity.getCreatedBy()));
        lookupService.lookupReviewerName(Arrays.asList(reviewResponse), ReviewResponse::getReviewerId, ReviewResponse::setReviewerName);

        return new PayloadResponse<>(request, ResponseCode.OK, reviewResponse);
    }

}
