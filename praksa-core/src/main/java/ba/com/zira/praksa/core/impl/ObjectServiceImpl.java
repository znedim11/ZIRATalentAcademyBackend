package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

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
import ba.com.zira.praksa.mapper.ObjectMapper;

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
        final List<ObjectEntity> objectList = objectModelEntities.getRecords();

        final List<ObjectResponse> objectResList = objectMapper.entityListToDtoList(objectList);

        PagedData<ObjectResponse> pagedData = new PagedData<>();
        pagedData.setNumberOfPages(objectModelEntities.getNumberOfPages());
        pagedData.setNumberOfRecords(objectModelEntities.getNumberOfRecords());
        pagedData.setPage(objectModelEntities.getPage());
        pagedData.setRecords(objectResList);
        pagedData.setRecordsPerPage(objectModelEntities.getRecordsPerPage());

        return new PagedPayloadResponse<>(request, ResponseCode.OK, pagedData);
    }

    @Override
    public PayloadResponse<ObjectResponse> create(EntityRequest<ObjectRequest> request) throws ApiException {
        requestValidator.validate(request);

        ObjectEntity objectEntity = objectMapper.dtoToEntity(request.getEntity());
        objectEntity.setCreated(LocalDateTime.now());
        objectEntity.setCreatedBy(request.getUserId());

        objectDAO.persist(objectEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, objectMapper.entityToDto(objectEntity));
    }

    @Override
    public PayloadResponse<ObjectResponse> findById(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        final ObjectEntity objectEntity = objectDAO.findByPK(request.getEntity());

        return new PayloadResponse<>(request, ResponseCode.OK, objectMapper.entityToDto(objectEntity));

    }

    @Override
    public PayloadResponse<ObjectRequest> update(final EntityRequest<ObjectRequest> request) throws ApiException {
        requestValidator.validate(request);

        final LocalDateTime date = LocalDateTime.now();
        final ObjectRequest object = request.getEntity();

        ObjectEntity entity = objectMapper.dtoToEntity(request.getEntity());

        entity.setModified(date);
        entity.setModifiedBy(request.getUserId());

        objectDAO.merge(entity);

        return new PayloadResponse<>(request, ResponseCode.OK, object);

    }

    @Override
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        objectDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Object deleted!");
    }
}
