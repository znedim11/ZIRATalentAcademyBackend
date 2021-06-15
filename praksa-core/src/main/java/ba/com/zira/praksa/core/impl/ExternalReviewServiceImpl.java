package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.ExternalReviewService;
import ba.com.zira.praksa.api.model.externalreview.ExternalReview;
import ba.com.zira.praksa.api.model.externalreview.ExternalReviewCreateRequest;
import ba.com.zira.praksa.api.model.externalreview.ExternalReviewUpdateRequest;
import ba.com.zira.praksa.core.validation.ExternalReviewRequestValidation;
import ba.com.zira.praksa.dao.ExternalReviewDAO;
import ba.com.zira.praksa.dao.RssFeedDAO;
import ba.com.zira.praksa.dao.model.ExternalReviewEntity;
import ba.com.zira.praksa.dao.model.RssFeedEntity;
import ba.com.zira.praksa.mapper.ExternalReviewMapper;

/**
 * @author zira
 *
 */

@Service
@ComponentScan
public class ExternalReviewServiceImpl implements ExternalReviewService {

    private static final String VALIDATE_ABSTRACT_REQUEST = "validateAbstractRequest";
    private RequestValidator requestValidator;
    private ExternalReviewRequestValidation externalReviewRequestValidation;
    private ExternalReviewDAO externalReviewDAO;
    private RssFeedDAO rssFeedDAO;
    private ExternalReviewMapper externalReviewMapper;

    public ExternalReviewServiceImpl(RequestValidator requestValidator, ExternalReviewRequestValidation externalReviewRequestValidation,
            ExternalReviewDAO externalReviewDAO, RssFeedDAO rssFeedDAO, ExternalReviewMapper externalReviewMapper) {
        super();
        this.requestValidator = requestValidator;
        this.externalReviewRequestValidation = externalReviewRequestValidation;
        this.externalReviewDAO = externalReviewDAO;
        this.rssFeedDAO = rssFeedDAO;
        this.externalReviewMapper = externalReviewMapper;
    }

    @Override
    public PagedPayloadResponse<ExternalReview> find(SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<ExternalReviewEntity> externalReviewPagedData = externalReviewDAO.findAll(request.getFilter());
        List<ExternalReviewEntity> externalReviewEntities = externalReviewPagedData.getRecords();

        final List<ExternalReview> externalReviewList = externalReviewMapper.entityListToExternalReviewList(externalReviewEntities);

        PagedData<ExternalReview> pagedData = new PagedData<>();
        pagedData.setNumberOfPages(externalReviewPagedData.getNumberOfPages());
        pagedData.setNumberOfRecords(externalReviewPagedData.getNumberOfRecords());
        pagedData.setPage(externalReviewPagedData.getPage());
        pagedData.setRecords(externalReviewList);
        pagedData.setRecordsPerPage(externalReviewPagedData.getRecordsPerPage());

        return new PagedPayloadResponse<>(request, ResponseCode.OK, pagedData);
    }

    @Override
    public PayloadResponse<ExternalReview> findById(SearchRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        externalReviewRequestValidation.validateExternalReviewExists(entityRequest, VALIDATE_ABSTRACT_REQUEST);

        final ExternalReviewEntity externalReviewEntity = externalReviewDAO.findByPK(request.getEntity());

        final ExternalReview externalReview = externalReviewMapper.entityToDto(externalReviewEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, externalReview);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<ExternalReview> create(EntityRequest<ExternalReviewCreateRequest> request) throws ApiException {
        EntityRequest<ExternalReviewCreateRequest> reviewEntityRequest = new EntityRequest<>(request.getEntity(), request);
        externalReviewRequestValidation.validateEntityExists(reviewEntityRequest, VALIDATE_ABSTRACT_REQUEST);

        ExternalReviewEntity entity = externalReviewMapper.createRequestToEntity(request.getEntity());
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(request.getUserId());

        EntityRequest<Long> rssFeedEntityRequest = new EntityRequest<>(request.getEntity().getRssFeedId(), request);
        externalReviewRequestValidation.validateRssFeedExists(rssFeedEntityRequest, VALIDATE_ABSTRACT_REQUEST);
        RssFeedEntity rssEntity = rssFeedDAO.findByPK(request.getEntity().getRssFeedId());
        entity.setRssFeed(rssEntity);

        externalReviewDAO.persist(entity);
        ExternalReview externalReview = externalReviewMapper.entityToDto(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, externalReview);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<ExternalReview> update(EntityRequest<ExternalReviewUpdateRequest> request) throws ApiException {
        externalReviewRequestValidation.validateEntityExists(request, "validateReviewExists");

        EntityRequest<Long> entityRequestId = new EntityRequest<>(request.getEntity().getId(), request);
        externalReviewRequestValidation.validateExternalReviewExists(entityRequestId, VALIDATE_ABSTRACT_REQUEST);

        final ExternalReviewUpdateRequest externalReviewUpdateRequest = request.getEntity();

        ExternalReviewEntity externalReviewEntity = externalReviewDAO.findByPK(request.getEntity().getId());
        externalReviewEntity = externalReviewMapper.updateRequestToEntity(externalReviewUpdateRequest, externalReviewEntity);

        EntityRequest<Long> rssFeedEntityRequest = new EntityRequest<>(request.getEntity().getRssFeedId(), request);
        externalReviewRequestValidation.validateRssFeedExists(rssFeedEntityRequest, VALIDATE_ABSTRACT_REQUEST);
        RssFeedEntity rssEntity = rssFeedDAO.findByPK(request.getEntity().getRssFeedId());
        externalReviewEntity.setRssFeed(rssEntity);

        externalReviewDAO.merge(externalReviewEntity);

        ExternalReview externalReview = externalReviewMapper.entityToDto(externalReviewEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, externalReview);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        externalReviewRequestValidation.validateExternalReviewExists(entityRequest, VALIDATE_ABSTRACT_REQUEST);

        externalReviewDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "External review deleted!");
    }

}
