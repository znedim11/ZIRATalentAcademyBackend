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
import ba.com.zira.praksa.api.model.externalReview.ExternalReview;
import ba.com.zira.praksa.api.model.externalReview.ExternalReviewCreateRequest;
import ba.com.zira.praksa.api.model.externalReview.ExternalReviewUpdateRequest;
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

    private RequestValidator requestValidator;
    private ExternalReviewRequestValidation externalReviewRequestValidation;
    private ExternalReviewDAO externalReviewDAO;
    private RssFeedDAO rssFeedDAO;
    private ExternalReviewMapper externalReviewMapper;

    public ExternalReviewServiceImpl(RequestValidator requestValidator, ExternalReviewRequestValidation externalReviewRequestValidation,
            ExternalReviewDAO externalReviewDAO, ExternalReviewMapper externalReviewMapper, RssFeedDAO rssFeedDAO) {
        this.requestValidator = requestValidator;
        this.externalReviewRequestValidation = externalReviewRequestValidation;
        this.externalReviewDAO = externalReviewDAO;
        this.externalReviewMapper = externalReviewMapper;
        this.rssFeedDAO = rssFeedDAO;
    }

    @Override
    public PagedPayloadResponse<ExternalReview> find(SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<ExternalReviewEntity> externalReviewPagedData = externalReviewDAO.findAll(request.getFilter());
        List<ExternalReviewEntity> externalReviewEntities = externalReviewPagedData.getRecords();

        final List<ExternalReview> externalReviewList = externalReviewMapper.entityListToExternalReviewList(externalReviewEntities);

        PagedData<ExternalReview> pagedData = new PagedData<ExternalReview>();
        pagedData.setNumberOfPages(externalReviewPagedData.getNumberOfPages());
        pagedData.setNumberOfRecords(externalReviewPagedData.getNumberOfRecords());
        pagedData.setPage(externalReviewPagedData.getPage());
        pagedData.setRecords(externalReviewList);
        pagedData.setRecordsPerPage(externalReviewPagedData.getRecordsPerPage());

        return new PagedPayloadResponse<>(request, ResponseCode.OK, pagedData);
    }

    @Override
    public PayloadResponse<ExternalReview> findById(SearchRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<Long>(request.getEntity(), request);
        externalReviewRequestValidation.validateExternalReviewExists(entityRequest, "validateAbstractRequest");

        final ExternalReviewEntity externalReviewEntity = externalReviewDAO.findByPK(request.getEntity());

        final ExternalReview externalReview = externalReviewMapper.entityToDto(externalReviewEntity);

        return new PayloadResponse<ExternalReview>(request, ResponseCode.OK, externalReview);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<ExternalReview> create(EntityRequest<ExternalReviewCreateRequest> request) throws ApiException {
        EntityRequest<ExternalReviewCreateRequest> reviewEntityRequest = new EntityRequest<ExternalReviewCreateRequest>(request.getEntity(),
                request);
        externalReviewRequestValidation.validateEntityExistsInCreateRequest(reviewEntityRequest, "validateAbstractRequest");

        ExternalReviewEntity entity = externalReviewMapper.createRequestToEntity(request.getEntity());
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(request.getUserId());

        EntityRequest<Long> rssFeedEntityRequest = new EntityRequest<Long>(request.getEntity().getRss_feed_id(), request);
        externalReviewRequestValidation.validateRssFeedExists(rssFeedEntityRequest, "validateAbstractRequest");
        RssFeedEntity rssEntity = rssFeedDAO.findByPK(request.getEntity().getRss_feed_id());
        entity.setRssFeed(rssEntity);

        externalReviewDAO.persist(entity);
        ExternalReview externalReview = externalReviewMapper.entityToDto(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, externalReview);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<ExternalReview> update(EntityRequest<ExternalReviewUpdateRequest> request) throws ApiException {
        externalReviewRequestValidation.validateEntityExistsInUpdateRequest(request, "validateReviewExists");

        EntityRequest<Long> entityRequestId = new EntityRequest<Long>(request.getEntity().getId(), request);
        externalReviewRequestValidation.validateExternalReviewExists(entityRequestId, "validateAbstractRequest");

        final ExternalReviewUpdateRequest externalReviewUpdateRequest = request.getEntity();

        ExternalReviewEntity externalReviewEntity = externalReviewDAO.findByPK(request.getEntity().getId());
        externalReviewEntity = externalReviewMapper.updateRequestToEntity(externalReviewUpdateRequest, externalReviewEntity);

        EntityRequest<Long> rssFeedEntityRequest = new EntityRequest<Long>(request.getEntity().getRss_feed_id(), request);
        externalReviewRequestValidation.validateRssFeedExists(rssFeedEntityRequest, "validateAbstractRequest");
        RssFeedEntity rssEntity = rssFeedDAO.findByPK(request.getEntity().getRss_feed_id());
        externalReviewEntity.setRssFeed(rssEntity);

        externalReviewDAO.merge(externalReviewEntity);

        ExternalReview externalReview = externalReviewMapper.entityToDto(externalReviewEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, externalReview);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<Long>(request.getEntity(), request);
        externalReviewRequestValidation.validateExternalReviewExists(entityRequest, "validateAbstractRequest");

        externalReviewDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "External review deleted!");
    }

}
