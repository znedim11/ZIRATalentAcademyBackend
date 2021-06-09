package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import ba.com.zira.praksa.api.model.feature.Feature;
import ba.com.zira.praksa.api.model.feature.FeatureCreateRequest;
import ba.com.zira.praksa.core.validation.FeatureRequestValidation;
import ba.com.zira.praksa.dao.FeatureDAO;
import ba.com.zira.praksa.dao.model.FeatureEntity;
import ba.com.zira.praksa.mapper.FeatureMapper;
import lombok.AllArgsConstructor;

/**
 * 
 * @author Ajas
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
    public PagedPayloadResponse<Feature> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<FeatureEntity> featureEntities = featureDAO.findAll(request.getFilter());
        final List<Feature> featureList = new ArrayList<Feature>();

        for (final FeatureEntity featureEntity : featureEntities.getRecords()) {
            featureList.add(featureMapper.entityToDto(featureEntity));
        }

        // TODO : Implement pagination || it's done somewhere ?
        return new PagedPayloadResponse<>(request, ResponseCode.OK, featureList.size(), 1, 1, featureList.size(), featureList);
    }

    @Override
    public PayloadResponse<Feature> findById(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);
        final FeatureEntity featureEntity = featureDAO.findByPK(request.getEntity());

        final Feature feature = featureMapper.entityToDto(featureEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, feature);
    }

    @Override
    public PayloadResponse<Feature> create(EntityRequest<FeatureCreateRequest> request) throws ApiException {
        requestValidator.validate(request);

        FeatureEntity featureEntity = featureMapper.dtoToEntity(request.getEntity());
        featureEntity.setCreated(LocalDateTime.now());
        featureEntity.setCreatedBy(request.getUserId());

        featureDAO.persist(featureEntity);
        Feature feature = featureMapper.entityToDto(featureEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, feature);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<Feature> update(final EntityRequest<Feature> request) throws ApiException {
        featureRequestValidation.validateFeatureUpdateRequest(request, "update");

        final Feature feature = request.getEntity();

        final FeatureEntity featureEntity = featureDAO.findByPK(feature.getId());
        featureMapper.updateDtoToEntity(feature, featureEntity);

        featureEntity.setModified(LocalDateTime.now());
        featureEntity.setModifiedBy(request.getUserId());

        featureDAO.merge(featureEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, feature);
    }

    @Override
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        featureDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Feature deleted!");
    }
}
