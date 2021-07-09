package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
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
import ba.com.zira.praksa.api.PlatformService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.platform.PlatformCreateRequest;
import ba.com.zira.praksa.api.model.platform.PlatformResponse;
import ba.com.zira.praksa.api.model.platform.PlatformUpdateRequest;
import ba.com.zira.praksa.core.validation.PlatformRequestValidation;
import ba.com.zira.praksa.dao.PlatformDAO;
import ba.com.zira.praksa.dao.model.PlatformEntity;
import ba.com.zira.praksa.mapper.PlatformMapper;

@Service
public class PlatformServiceImpl implements PlatformService {
    static final String VALIDATE_ABSTRACT_REQUEST = "validateAbstractRequest";
    PlatformRequestValidation platformRequestValidation;
    RequestValidator requestValidator;
    PlatformDAO platformDAO;
    PlatformMapper platformMapper;

    public PlatformServiceImpl(PlatformRequestValidation platformRequestValidation, RequestValidator requestValidator,
            PlatformDAO platformDAO, PlatformMapper platformMapper) {
        super();
        this.platformRequestValidation = platformRequestValidation;
        this.requestValidator = requestValidator;
        this.platformDAO = platformDAO;
        this.platformMapper = platformMapper;
    }

    @Override
    public PagedPayloadResponse<PlatformResponse> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<PlatformEntity> platformModelEntities = platformDAO.findAll(request.getFilter());
        final List<PlatformResponse> platformList = new ArrayList<>();

        for (final PlatformEntity PlatformEntity : platformModelEntities.getRecords()) {
            platformList.add(platformMapper.entityToDto(PlatformEntity));
        }
        return new PagedPayloadResponse<>(request, ResponseCode.OK, platformList.size(), 1, 1, platformList.size(), platformList);
    }

    @Override
    public PayloadResponse<PlatformResponse> create(EntityRequest<PlatformCreateRequest> request) throws ApiException {
        requestValidator.validate(request);
        PlatformEntity entity = platformMapper.dtoToEntity(request.getEntity());
        platformDAO.persist(entity);
        PlatformResponse response = platformMapper.entityToDto(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    public PayloadResponse<PlatformResponse> findById(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        final PlatformEntity entity = platformDAO.findByPK(request.getEntity());

        final PlatformResponse response = platformMapper.entityToDto(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Transactional(rollbackFor = ApiException.class)
    @Override
    public PayloadResponse<PlatformResponse> update(final EntityRequest<PlatformUpdateRequest> request) throws ApiException {
        platformRequestValidation.validateUpdatePlatformRequest(request, "update");

        PlatformEntity existingPlatformEntity = platformDAO.findByPK(request.getEntity().getId());

        final LocalDateTime date = LocalDateTime.now();
        final PlatformUpdateRequest platform = request.getEntity();

        platformMapper.updateForPlatformUpdate(platform, existingPlatformEntity);

        existingPlatformEntity.setModified(date);
        existingPlatformEntity.setModifiedBy(request.getUserId());

        platformDAO.merge(existingPlatformEntity);

        final PlatformResponse response = platformMapper.entityToDto(existingPlatformEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);

    }

    @Override
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        platformDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Platform deleted!");
    }

    @Override
    public ListPayloadResponse<LoV> getLoVs(final ListRequest<Long> request) throws ApiException {
        List<LoV> loVs = platformDAO.getLoVs(request.getList());

        return new ListPayloadResponse<>(request, ResponseCode.OK, loVs);
    }
}
