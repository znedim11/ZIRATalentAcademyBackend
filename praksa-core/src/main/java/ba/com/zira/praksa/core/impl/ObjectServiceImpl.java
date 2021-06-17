package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.ObjectService;
import ba.com.zira.praksa.api.model.object.ObjectRequest;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.dao.ObjectDAO;
import ba.com.zira.praksa.dao.model.ObjectEntity;

@Service
public class ObjectServiceImpl implements ObjectService {

    private RequestValidator requestValidator;
    private ObjectDAO objectDAO;
    private ObjectMapper objectMapper;

    public ObjectServiceImpl(final RequestValidator requestValidator, ObjectDAO objectDAO, ObjectMapper objectMapper) {
        this.requestValidator = requestValidator;
        this.objectDAO = objectDAO;
        this.objectMapper = objectMapper;
    }

    @Override
    public PagedPayloadResponse<ObjectResponse> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<ObjectEntity> objectModelEntities = objectDAO.findAll(request.getFilter());
        final List<ObjectResponse> objectList = new ArrayList<>();

        // todo

        return new PagedPayloadResponse<>(request, ResponseCode.OK, objectList.size(), 1, 1, objectList.size(), objectList);
    }

    @Override
    public PayloadResponse<ObjectResponse> create(EntityRequest<ObjectRequest> request) throws ApiException {
        requestValidator.validate(request);

        // todo

        ObjectResponse response = new ObjectResponse();

        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    public PayloadResponse<ObjectResponse> findById(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        final ObjectEntity objectEntity = objectDAO.findByPK(request.getEntity());

        // todo
        // final ObjectResponse object = objectMapper.entityToDto(objectEntity);
        final ObjectResponse object = new ObjectResponse();
        return new PayloadResponse<>(request, ResponseCode.OK, object);
    }

    @Transactional(rollbackFor = ApiException.class)
    @Override
    public PayloadResponse<ObjectRequest> update(final EntityRequest<ObjectRequest> request) throws ApiException {
        requestValidator.validate(request);

        final LocalDateTime date = LocalDateTime.now();
        final ObjectRequest object = request.getEntity();

        // todo
        // final ObjectEntity objectEntity = objectMapper.dtoToEntity(object);
        final ObjectEntity objectEntity = new ObjectEntity();

        objectDAO.merge(objectEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, object);

    }

    @Override
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        objectDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Object deleted!");
    }
}
