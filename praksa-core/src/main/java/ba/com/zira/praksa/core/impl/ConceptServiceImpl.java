/**
 *
 */
package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
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
import ba.com.zira.praksa.api.ConceptService;
import ba.com.zira.praksa.api.model.concept.ConceptCreateRequest;
import ba.com.zira.praksa.api.model.concept.ConceptResponse;
import ba.com.zira.praksa.api.model.concept.ConceptUpdateRequest;
import ba.com.zira.praksa.core.validation.ConceptRequestValidation;
import ba.com.zira.praksa.dao.ConceptDAO;
import ba.com.zira.praksa.dao.model.ConceptEntity;
import ba.com.zira.praksa.mapper.ConceptMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author zira
 *
 */

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConceptServiceImpl implements ConceptService {

    static final String VALIDATE_ABSTRACT_REQUEST = "validateAbstractRequest";
    static final String BASIC_NOT_NULL = "basicNotNull";
    RequestValidator requestValidator;
    ConceptRequestValidation conceptRequestValidation;
    ConceptDAO conceptDAO;
    ConceptMapper conceptMapper;

    @Override
    public PagedPayloadResponse<ConceptResponse> find(SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<ConceptEntity> conceptEntitesData = conceptDAO.findAll(request.getFilter());
        List<ConceptEntity> conceptEntities = conceptEntitesData.getRecords();

        final List<ConceptResponse> conceptList = conceptMapper.entityListToResponseList(conceptEntities);

        PagedData<ConceptResponse> pagedData = new PagedData<>();
        pagedData.setNumberOfPages(conceptEntitesData.getNumberOfPages());
        pagedData.setNumberOfRecords(conceptEntitesData.getNumberOfRecords());
        pagedData.setPage(conceptEntitesData.getPage());
        pagedData.setRecords(conceptList);
        pagedData.setRecordsPerPage(conceptEntitesData.getRecordsPerPage());

        return new PagedPayloadResponse<>(request, ResponseCode.OK, pagedData);
    }

    @Override
    public PayloadResponse<ConceptResponse> findById(SearchRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        conceptRequestValidation.validateConceptExists(entityRequest, VALIDATE_ABSTRACT_REQUEST);

        final ConceptEntity conceptEntity = conceptDAO.findByPK(request.getEntity());

        final ConceptResponse conceptResponse = conceptMapper.entityToResponse(conceptEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, conceptResponse);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<ConceptResponse> create(EntityRequest<ConceptCreateRequest> request) throws ApiException {
        conceptRequestValidation.validateEntityExistsInCreateRequest(request, BASIC_NOT_NULL);

        EntityRequest<String> entityRequest = new EntityRequest<>(request.getEntity().getName(), request);
        conceptRequestValidation.validateConceptNameExists(entityRequest, BASIC_NOT_NULL);

        ConceptEntity entity = conceptMapper.createRequestToEntity(request.getEntity());
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(request.getUserId());

        conceptDAO.persist(entity);

        ConceptResponse response = conceptMapper.entityToResponse(entity);

        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<ConceptResponse> update(final EntityRequest<ConceptUpdateRequest> request) throws ApiException {
        conceptRequestValidation.validateEntityExistsInUpdateRequest(request, BASIC_NOT_NULL);

        EntityRequest<Long> entityRequestId = new EntityRequest<>(request.getEntity().getId(), request);
        conceptRequestValidation.validateConceptExists(entityRequestId, VALIDATE_ABSTRACT_REQUEST);

        EntityRequest<String> entityRequestName = new EntityRequest<>(request.getEntity().getName(), request);
        conceptRequestValidation.validateConceptNameExists(entityRequestName, BASIC_NOT_NULL);

        final ConceptUpdateRequest conceptRequest = request.getEntity();

        ConceptEntity conceptEntity = conceptDAO.findByPK(request.getEntity().getId());
        conceptEntity = conceptMapper.updateRequestToEntity(conceptRequest, conceptEntity);
        conceptEntity.setModified(LocalDateTime.now());
        conceptEntity.setModifiedBy(request.getUserId());

        conceptDAO.merge(conceptEntity);

        final ConceptResponse conceptResponse = conceptMapper.entityToResponse(conceptEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, conceptResponse);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        conceptRequestValidation.validateConceptExists(entityRequest, VALIDATE_ABSTRACT_REQUEST);

        conceptDAO.removeByPK(request.getEntity());

        return new PayloadResponse<>(request, ResponseCode.OK, "Concept deleted!");
    }

}
