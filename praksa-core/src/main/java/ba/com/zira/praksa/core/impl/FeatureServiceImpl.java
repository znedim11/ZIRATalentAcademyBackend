package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;

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
import ba.com.zira.praksa.api.FeatureService;
import ba.com.zira.praksa.api.model.feature.FeatureCreateRequest;
import ba.com.zira.praksa.api.model.feature.FeatureResponse;
import ba.com.zira.praksa.api.model.feature.FeatureUpdateRequest;
import ba.com.zira.praksa.core.validation.FeatureRequestValidation;
import ba.com.zira.praksa.dao.FeatureDAO;
import ba.com.zira.praksa.dao.model.FeatureEntity;
import ba.com.zira.praksa.mapper.FeatureMapper;
import lombok.AllArgsConstructor;

/**
 * 
 * @author zira
 *
 */
@Service
@AllArgsConstructor
public class FeatureServiceImpl implements FeatureService {
    private RequestValidator requestValidator;
    private FeatureRequestValidation featureRequestValidation;
    private FeatureDAO featureDAO;
    private FeatureMapper featureMapper;

    @Override
    public PagedPayloadResponse<FeatureResponse> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<FeatureEntity> featureEntitiesPaged = featureDAO.findAll(request.getFilter());

        final PagedData<FeatureResponse> featuresPaged = featureMapper.entitiesToDtos(featureEntitiesPaged);

        return new PagedPayloadResponse<>(request, ResponseCode.OK, featuresPaged);
    }

    @Override
    public PayloadResponse<FeatureResponse> findById(final SearchRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<Long>(request.getEntity(), request);
        featureRequestValidation.validateIfFeatureExists(entityRequest, "validateAbstractRequest");

        final FeatureEntity featureEntity = featureDAO.findByPK(request.getEntity());

        final FeatureResponse feature = featureMapper.entityToDto(featureEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, feature);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<FeatureResponse> create(EntityRequest<FeatureCreateRequest> request) throws ApiException {
        featureRequestValidation.validateFeatureRequestFields(request, "basicNotNull");

        FeatureEntity featureEntity = featureMapper.dtoToEntity(request.getEntity());
        featureEntity.setCreated(LocalDateTime.now());
        featureEntity.setCreatedBy(request.getUserId());

        featureDAO.persist(featureEntity);
        FeatureResponse feature = featureMapper.entityToDto(featureEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, feature);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<FeatureResponse> update(final EntityRequest<FeatureUpdateRequest> request) throws ApiException {
        featureRequestValidation.validateBasicFeatureRequest(request);

        final FeatureEntity featureEntity = featureDAO.findByPK(request.getEntity().getId());
        featureMapper.updateDtoToEntity(request.getEntity(), featureEntity);

        featureEntity.setModified(LocalDateTime.now());
        featureEntity.setModifiedBy(request.getUserId());

        featureDAO.merge(featureEntity);
        final FeatureResponse feature = featureMapper.entityToDto(featureEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, feature);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<Long>(request.getEntity(), request);
        featureRequestValidation.validateIfFeatureExists(entityRequest, "validateAbstractRequest");

        featureDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Feature deleted!");
    }
}
