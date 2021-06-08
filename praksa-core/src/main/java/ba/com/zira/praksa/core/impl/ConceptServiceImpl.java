/**
 *
 */
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
    private ConceptDAO conceptDAO;
    private ConceptMapper conceptMapper;

    public ConceptServiceImpl(ConceptDAO conceptDAO, ConceptMapper conceptMapper) {
        this.conceptDAO = conceptDAO;
        this.conceptMapper = conceptMapper;
    }

    @Override
    public PagedPayloadResponse<Concept> find(SearchRequest<String> request) throws ApiException {
        PagedData<ConceptEntity> conceptEntites = conceptDAO.findAll(request.getFilter());
        final List<Concept> conceptList = new ArrayList<Concept>();

        for (final ConceptEntity conceptEntity : conceptEntites.getRecords()) {
            conceptList.add(conceptMapper.entityToDto(conceptEntity));
        }

        return new PagedPayloadResponse<Concept>(request, ResponseCode.OK, conceptList.size(), 1, 1, conceptList.size(), conceptList);
    }

    @Override
    public PayloadResponse<Concept> findById(SearchRequest<Long> request) throws ApiException {
        final ConceptEntity conceptEntity = conceptDAO.findByPK(request.getEntity());

        final Concept concept = conceptMapper.entityToDto(conceptEntity);

        return new PayloadResponse<Concept>(request, ResponseCode.OK, concept);
    }

    @Override
    public PayloadResponse<Concept> create(EntityRequest<Concept> request) throws ApiException {
        ConceptEntity entity = conceptMapper.dtoToEntity(request.getEntity());

        conceptDAO.persist(entity);

        Concept response = conceptMapper.entityToDto(entity);

        return new PayloadResponse<Concept>(request, ResponseCode.OK, response);
    }

    @Override
    public PayloadResponse<Concept> update(EntityRequest<Concept> request) throws ApiException {

        final LocalDateTime dateTime = LocalDateTime.now();
        final Concept concept = request.getEntity();
        final ConceptEntity conceptEntity = conceptMapper.dtoToEntity(concept);

        conceptDAO.merge(conceptEntity);

        return new PayloadResponse<Concept>(request, ResponseCode.OK, concept);
    }

    @Transactional(rollbackFor = ApiException.class)
    @Override
    public PayloadResponse<String> delete(EntityRequest<Long> request) throws ApiException {
        conceptDAO.removeByPK(request.getEntity());

        return new PayloadResponse<String>(request, ResponseCode.OK, "Concept deleted!");
    }

}
