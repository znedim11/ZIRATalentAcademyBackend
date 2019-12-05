package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.Request;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.message.response.ValidationResponse;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.commons.model.enums.Status;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.mapper.SampleModelMapper;
import ba.com.zira.praksa.dao.SampleDAO;
import ba.com.zira.praksa.api.SampleService;
import ba.com.zira.praksa.api.model.SampleModel;
import ba.com.zira.praksa.api.request.SampleRequest;
import ba.com.zira.praksa.api.response.SampleResponse;
import ba.com.zira.praksa.dao.model.SampleModelEntity;

@Service
public class SampleServiceImpl implements SampleService {

    private RequestValidator requestValidator;
    private SampleDAO sampleDAO;
    private SampleModelMapper sampleModelMapper;

    public SampleServiceImpl(final RequestValidator requestValidator, SampleDAO sampleDAO, SampleModelMapper sampleModelMapper) {
        this.requestValidator = requestValidator;
        this.sampleDAO = sampleDAO;
        this.sampleModelMapper = sampleModelMapper;
    }
    
    @Override
    public PagedPayloadResponse<SampleResponse> find(final Request request) throws ApiException {
            requestValidator.validate(request);
            
            PagedData<SampleModelEntity> sampleModelEntities = sampleDAO.findAll(request.getFilter());
            final List<SampleResponse> sampleResponseList = new ArrayList<>();

            for (final SampleModelEntity sampleModelEntity : sampleModelEntities.getRecords()) {
                sampleResponseList.add(sampleModelMapper.entityToDto(sampleModelEntity));
            }
            final PagedPayloadResponse<SampleResponse> response = new PagedPayloadResponse<>(request, ResponseCode.OK, sampleResponseList.size(), 1, 1,
                    sampleResponseList.size(), sampleResponseList);
            return response;
    }
    @Override
    public PayloadResponse<SampleResponse> createSample(EntityRequest<SampleRequest> request) throws ApiException {
        requestValidator.validate(request);
        SampleRequest sample = request.getEntity();
        SampleModelEntity entity = sampleModelMapper.dtoToEntity(sample);
        sampleDAO.persist(entity);
        SampleResponse response = sampleModelMapper.entityToDto(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }
    @Override
    public PayloadResponse<SampleResponse> findById(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        final SampleModelEntity sampleEntity = sampleDAO.findByPK(Long.parseLong(request.getEntity()));

        final SampleResponse sample = sampleModelMapper.entityToDto(sampleEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, sample);
    }
    @Transactional(rollbackFor = ApiException.class)
    @Override
    public PayloadResponse<SampleResponse> update(final EntityRequest<SampleRequest> request) throws ApiException {
        requestValidator.validate(request);

        final LocalDateTime date = LocalDateTime.now();
        final SampleRequest sampleRequest = request.getEntity();
        final SampleModelEntity sampleEntity = sampleModelMapper.dtoToEntity(sampleRequest);

        final SampleResponse sampleResponse = sampleModelMapper.entityToDto(sampleEntity);
        
        sampleDAO.merge(sampleEntity);
        
        return new PayloadResponse<>(request, ResponseCode.OK, sampleResponse);

    }
    @Override
    public void delete(final EntityRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        sampleDAO.removeByPK(Long.decode(request.getEntity()));
    }
}
