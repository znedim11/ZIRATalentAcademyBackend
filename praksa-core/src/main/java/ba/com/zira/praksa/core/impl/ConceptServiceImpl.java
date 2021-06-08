/**
 *
 */
package ba.com.zira.praksa.core.impl;

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
import ba.com.zira.praksa.api.ConceptService;
import ba.com.zira.praksa.api.model.concept.Concept;
import ba.com.zira.praksa.dao.ConceptDAO;
import ba.com.zira.praksa.dao.model.ConceptEntity;
import ba.com.zira.praksa.mapper.ConceptMapper;

/**
 * @author irma
 *
 */

@Service
public class ConceptServiceImpl implements ConceptService {

    private RequestValidator requestValidator;
    private ConceptDAO conceptDAO;
    private ConceptMapper conceptMapper;

    public ConceptServiceImpl(RequestValidator requestValidator, ConceptDAO conceptDAO, ConceptMapper conceptMapper) {
        this.requestValidator = requestValidator;
        this.conceptDAO = conceptDAO;
        this.conceptMapper = conceptMapper;
    }

    @Override
    public PagedPayloadResponse<Concept> find(SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<ConceptEntity> conceptEntites = conceptDAO.findAll(request.getFilter());
        final List<Concept> conceptList = new ArrayList<Concept>();

        for (final ConceptEntity conceptEntity : conceptEntites.getRecords()) {
            conceptList.add(conceptMapper.entityToDto(conceptEntity));
        }

        return new PagedPayloadResponse<Concept>(request, ResponseCode.OK, conceptList.size(), 1, 1, conceptList.size(), conceptList);
    }

    @Override
    public PayloadResponse<Concept> findById(SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        final ConceptEntity conceptEntity = conceptDAO.findByPK(request.getEntity());

        final Concept concept = conceptMapper.entityToDto(conceptEntity);

        return new PayloadResponse<Concept>(request, ResponseCode.OK, concept);
    }

    @Override
    public PayloadResponse<Concept> create(EntityRequest<Concept> request) throws ApiException {
        requestValidator.validate(request);

        ConceptEntity entity = conceptMapper.dtoToEntity(request.getEntity());

        conceptDAO.persist(entity);

        Concept response = conceptMapper.entityToDto(entity);

        return new PayloadResponse<Concept>(request, ResponseCode.OK, response);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<Concept> update(EntityRequest<Concept> request) throws ApiException {
        requestValidator.validate(request);

        final Concept concept = request.getEntity();
        final ConceptEntity conceptEntity = conceptMapper.dtoToEntity(concept);

        conceptDAO.merge(conceptEntity);

        return new PayloadResponse<Concept>(request, ResponseCode.OK, concept);
    }

    @Override
    public PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        conceptDAO.removeByPK(request.getEntity());

        return new PayloadResponse<String>(request, ResponseCode.OK, "Concept deleted!");
    }

}
