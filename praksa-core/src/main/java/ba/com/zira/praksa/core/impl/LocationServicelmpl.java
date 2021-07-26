package ba.com.zira.praksa.core.impl;

import java.util.ArrayList;
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
import ba.com.zira.praksa.api.LocationService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.enums.ObjectType;
import ba.com.zira.praksa.api.model.location.Location;
import ba.com.zira.praksa.core.validation.LocationRequestValidation;
import ba.com.zira.praksa.dao.LocationDAO;
import ba.com.zira.praksa.dao.model.CharacterEntity_;
import ba.com.zira.praksa.dao.model.ConceptEntity_;
import ba.com.zira.praksa.dao.model.GameEntity_;
import ba.com.zira.praksa.dao.model.LinkMapEntity_;
import ba.com.zira.praksa.dao.model.LocationEntity;
import ba.com.zira.praksa.dao.model.ObjectEntity_;
import ba.com.zira.praksa.dao.model.PersonEntity_;
import ba.com.zira.praksa.mapper.LocationMapper;

@Service
public class LocationServicelmpl implements LocationService {

    private RequestValidator requestValidator;
    private LocationDAO locationDAO;
    private LocationMapper locationMapper;
    private LocationRequestValidation locationRequestValidation;

    public LocationServicelmpl(RequestValidator requestValidator, LocationDAO locationDAO, LocationMapper locationMapper,
            LocationRequestValidation locationRequestValidation) {
        super();
        this.requestValidator = requestValidator;
        this.locationDAO = locationDAO;
        this.locationMapper = locationMapper;
        this.locationRequestValidation = locationRequestValidation;
    }

    @Override
    public PayloadResponse<Location> create(EntityRequest<Location> request) throws ApiException {
        requestValidator.validate(request);
        LocationEntity entity = locationMapper.dtoToEntity(request.getEntity());
        locationDAO.persist(entity);
        Location response = locationMapper.entityToDto(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        locationDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Location deleted!");
    }

    @Override
    public PagedPayloadResponse<Location> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<LocationEntity> locationModelEntities = locationDAO.findAll(request.getFilter());
        final List<Location> locationList = new ArrayList<>();

        for (final LocationEntity LocationEntity : locationModelEntities.getRecords()) {
            locationList.add(locationMapper.entityToDto(LocationEntity));
        }
        return new PagedPayloadResponse<>(request, ResponseCode.OK, locationList.size(), 1, 1, locationList.size(), locationList);
    }

    @Override
    public PayloadResponse<Location> findById(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        final LocationEntity locationEntity = locationDAO.findByPK(request.getEntity());

        final Location location = locationMapper.entityToDto(locationEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, location);
    }

    @Transactional(rollbackFor = ApiException.class)
    @Override
    public PayloadResponse<Location> update(final EntityRequest<Location> request) throws ApiException {
        requestValidator.validate(request);

        final Location location = request.getEntity();
        final LocationEntity locationEntity = locationMapper.dtoToEntity(location);

        locationDAO.merge(locationEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, location);

    }

    @Override
    public ListPayloadResponse<LoV> getLoVs(final ListRequest<Long> request) throws ApiException {
        if (request.getList() != null) {
            for (Long item : request.getList()) {
                EntityRequest<Long> longRequest = new EntityRequest<>(item, request);
                locationRequestValidation.validateLocationExists(longRequest, "validateAbstractRequest");
            }
        }

        List<LoV> loVs = locationDAO.getLoVs(request.getList());

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
        } else if (ObjectType.OBJECT.getValue().equalsIgnoreCase(request.getEntity().getName())) {
            field = LinkMapEntity_.object.getName();
            idField = ObjectEntity_.id.getName();
        } else if (ObjectType.PERSON.getValue().equalsIgnoreCase(request.getEntity().getName())) {
            field = LinkMapEntity_.person.getName();
            idField = PersonEntity_.id.getName();
        }

        List<LoV> loVs = null;
        if (field != null) {
            loVs = locationDAO.getLoVsNotConnectedTo(field, idField, id);
        }

        return new ListPayloadResponse<>(request, ResponseCode.OK, loVs);
    }
}
