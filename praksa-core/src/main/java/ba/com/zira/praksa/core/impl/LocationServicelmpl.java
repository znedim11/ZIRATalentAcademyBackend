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
import ba.com.zira.praksa.api.LocationService;
import ba.com.zira.praksa.api.model.location.Location;
import ba.com.zira.praksa.dao.LocationDAO;
import ba.com.zira.praksa.dao.model.LocationEntity;
import ba.com.zira.praksa.mapper.LocationMapper;

@Service
public class LocationServicelmpl implements LocationService {

    private RequestValidator requestValidator;
    private LocationDAO locationDAO;
    private LocationMapper locationMapper;

    public LocationServicelmpl(final RequestValidator requestValidator, LocationDAO locationDAO, LocationMapper locationMapper) {
        this.requestValidator = requestValidator;
        this.locationDAO = locationDAO;
        this.locationMapper = locationMapper;
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
}
