package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.commons.validation.RequestValidator;
import ba.com.zira.praksa.api.ObjectService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.api.model.object.ObjectCreateRequest;
import ba.com.zira.praksa.api.model.object.ObjectResponse;
import ba.com.zira.praksa.api.model.object.ObjectUpdateRequest;
import ba.com.zira.praksa.core.validation.ObjectRequestValidation;
import ba.com.zira.praksa.dao.ObjectDAO;
import ba.com.zira.praksa.dao.model.CharacterEntity_;
import ba.com.zira.praksa.dao.model.ConceptEntity_;
import ba.com.zira.praksa.dao.model.GameEntity_;
import ba.com.zira.praksa.dao.model.LinkMapEntity_;
import ba.com.zira.praksa.dao.model.LocationEntity_;
import ba.com.zira.praksa.dao.model.ObjectEntity;
import ba.com.zira.praksa.dao.model.PersonEntity_;
import ba.com.zira.praksa.mapper.ObjectMapper;

/**
 *
 * @author zira
 *
 */
@Service
public class ObjectServiceImpl implements ObjectService {

    private RequestValidator requestValidator;
    private ObjectRequestValidation objectRequestValidation;
    private ObjectDAO objectDAO;
    private ObjectMapper objectMapper;

    public ObjectServiceImpl(final RequestValidator requestValidator, ObjectRequestValidation objectRequestValidation, ObjectDAO objectDAO,
            ObjectMapper objectMapper) {
        super();
        this.requestValidator = requestValidator;
        this.objectRequestValidation = objectRequestValidation;
        this.objectDAO = objectDAO;
        this.objectMapper = objectMapper;
    }

    @Override
    public PagedPayloadResponse<ObjectResponse> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<ObjectEntity> objectModelEntities = objectDAO.findAll(request.getFilter());
        final PagedData<ObjectResponse> objectsPaged = objectMapper.entitiesToDtos(objectModelEntities);

        return new PagedPayloadResponse<>(request, ResponseCode.OK, objectsPaged);
    }

    @Override
    public PayloadResponse<ObjectResponse> findById(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        objectRequestValidation.validateObjectExists(entityRequest, "validateAbstractRequest");

        final ObjectEntity objectEntity = objectDAO.findByPK(request.getEntity());
        final ObjectResponse object = objectMapper.entityToDto(objectEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, object);

    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<ObjectResponse> create(EntityRequest<ObjectCreateRequest> request) throws ApiException {
        objectRequestValidation.validateObjectRequestFields(request, "basicNotNull");

        ObjectEntity objectEntity = objectMapper.dtoToEntity(request.getEntity());
        objectEntity.setCreated(LocalDateTime.now());
        objectEntity.setCreatedBy(request.getUserId());

        objectDAO.persist(objectEntity);
        ObjectResponse object = objectMapper.entityToDto(objectEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, object);

    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<ObjectResponse> update(final EntityRequest<ObjectUpdateRequest> request) throws ApiException {

        objectRequestValidation.validateObjectUpdate(request);

        final ObjectEntity objectEntity = objectDAO.findByPK(request.getEntity().getId());
        objectMapper.updateDtoToEntity(request.getEntity(), objectEntity);

        objectEntity.setModified(LocalDateTime.now());
        objectEntity.setModifiedBy(request.getUserId());

        objectDAO.merge(objectEntity);
        final ObjectResponse object = objectMapper.entityToDto(objectEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, object);

    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        EntityRequest<Long> entityRequest = new EntityRequest<>(request.getEntity(), request);
        objectRequestValidation.validateObjectExists(entityRequest, "validateAbstractRequest");

        objectDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Object deleted!");
    }

    @Override
    public ListPayloadResponse<LoV> getLoVs(final ListRequest<Long> request) throws ApiException {
        if (request.getList() != null) {
            for (Long item : request.getList()) {
                EntityRequest<Long> longRequest = new EntityRequest<>(item, request);
                objectRequestValidation.validateObjectExists(longRequest, "validateAbstractRequest");
            }
        }

        List<LoV> loVs = objectDAO.getLoVs(request.getList());

        return new ListPayloadResponse<>(request, ResponseCode.OK, loVs);
    }

    @Override
    public ListPayloadResponse<LoV> getLoVsNotConnectedTo(final EntityRequest<LoV> request) throws ApiException {
        requestValidator.validate(request);

        String field = null;
        String idField = null;
        Long id = request.getEntity().getId();

        if (ObjectType.CHARACTER.getValue().equalsIgnoreCase(request.getEntity().getName())) {
            field = LinkMapEntity_.character.getName();
            idField = CharacterEntity_.id.getName();
        } else if (ObjectType.CONCEPT.getValue().equalsIgnoreCase(request.getEntity().getName())) {
            field = LinkMapEntity_.concept.getName();
            idField = ConceptEntity_.id.getName();
        } else if (ObjectType.GAME.getValue().equalsIgnoreCase(request.getEntity().getName())) {
            field = LinkMapEntity_.game.getName();
            idField = GameEntity_.id.getName();
        } else if (ObjectType.LOCATION.getValue().equalsIgnoreCase(request.getEntity().getName())) {
            field = LinkMapEntity_.location.getName();
            idField = LocationEntity_.id.getName();
        } else if (ObjectType.PERSON.getValue().equalsIgnoreCase(request.getEntity().getName())) {
            field = LinkMapEntity_.person.getName();
            idField = PersonEntity_.id.getName();
        }

        List<LoV> loVs = null;
        if (field != null) {
            loVs = objectDAO.getLoVsNotConnectedTo(field, idField, id);
        }

        return new ListPayloadResponse<>(request, ResponseCode.OK, loVs);
    }

}
