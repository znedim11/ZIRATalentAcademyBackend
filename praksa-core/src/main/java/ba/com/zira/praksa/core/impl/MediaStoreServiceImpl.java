package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
import ba.com.zira.praksa.api.MediaStoreService;
import ba.com.zira.praksa.api.model.mediastore.MediaStoreCreateRequest;
import ba.com.zira.praksa.api.model.mediastore.MediaStoreResponse;
import ba.com.zira.praksa.api.model.mediastore.MediaStoreUpdateRequest;
import ba.com.zira.praksa.core.validation.MediaStoreRequestValidation;
import ba.com.zira.praksa.dao.MediaDAO;
import ba.com.zira.praksa.dao.MediaStoreDAO;
import ba.com.zira.praksa.dao.model.MediaEntity;
import ba.com.zira.praksa.dao.model.MediaStoreEntity;
import ba.com.zira.praksa.mapper.MediaStoreMapper;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MediaStoreServiceImpl implements MediaStoreService {

    private RequestValidator requestValidator;
    private MediaStoreRequestValidation mediaStoreRequestValidation;
    private MediaStoreDAO mediaStoreDAO;
    private MediaDAO mediaDAO;
    private MediaStoreMapper mediaStoreMapper;

    @Override
    public PagedPayloadResponse<MediaStoreResponse> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<MediaStoreEntity> mediaStoreEntitesData = mediaStoreDAO.findAll(request.getFilter());
        List<MediaStoreEntity> mediaStoreEntities = mediaStoreEntitesData.getRecords();

        final List<MediaStoreResponse> mediaStoreList = mediaStoreMapper.entityListToResponseList(mediaStoreEntities);

        PagedData<MediaStoreResponse> pagedData = new PagedData<MediaStoreResponse>();
        pagedData.setNumberOfPages(mediaStoreEntitesData.getNumberOfPages());
        pagedData.setNumberOfRecords(mediaStoreEntitesData.getNumberOfRecords());
        pagedData.setPage(mediaStoreEntitesData.getPage());
        pagedData.setRecords(mediaStoreList);
        pagedData.setRecordsPerPage(mediaStoreEntitesData.getRecordsPerPage());

        return new PagedPayloadResponse<MediaStoreResponse>(request, ResponseCode.OK, pagedData);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<MediaStoreResponse> create(EntityRequest<MediaStoreCreateRequest> request) throws ApiException {
        mediaStoreRequestValidation.validateEntityExistsInCreateRequest(request, "basicNotNull");

        EntityRequest<String> entityRequest = new EntityRequest<String>(request.getEntity().getName(), request);
        mediaStoreRequestValidation.validateMediaStoreNameExists(entityRequest, "basicNotNull");

        MediaStoreEntity entity = mediaStoreMapper.createRequestToEntity(request.getEntity());
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(request.getUserId());

        UUID uuid = UUID.randomUUID();
        String uuidToString = uuid.toString();
        entity.setUuid(uuidToString);

        EntityRequest<Long> mediaEntityRequest = new EntityRequest<Long>(request.getEntity().getMediaId(), request);
        mediaStoreRequestValidation.validateMediaIdExists(mediaEntityRequest, "basicNotNull");
        MediaEntity media = mediaDAO.findByPK(request.getEntity().getMediaId());
        entity.setMedia(media);

        mediaStoreDAO.merge(entity);

        MediaStoreResponse response = mediaStoreMapper.entityToResponse(entity);

        return new PayloadResponse<MediaStoreResponse>(request, ResponseCode.OK, response);

    }

    @Override
    public PayloadResponse<MediaStoreResponse> findByUuid(final EntityRequest<String> request) throws ApiException {
        mediaStoreRequestValidation.validateMediaStoreExists(request, "validateAbstractRequest");

        final MediaStoreEntity mediaStoreEntity = mediaStoreDAO.findByPK(request.getEntity());

        final MediaStoreResponse mediaStoreResponse = mediaStoreMapper.entityToResponse(mediaStoreEntity);

        return new PayloadResponse<MediaStoreResponse>(request, ResponseCode.OK, mediaStoreResponse);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<MediaStoreResponse> update(final EntityRequest<MediaStoreUpdateRequest> request) throws ApiException {
        mediaStoreRequestValidation.validateEntityExistsInUpdateRequest(request, "basicNotNull");

        EntityRequest<String> entityRequestUuid = new EntityRequest<String>(request.getEntity().getUuid(), request);
        mediaStoreRequestValidation.validateMediaStoreExists(entityRequestUuid, "validateAbstractRequest");

        EntityRequest<String> entityRequestName = new EntityRequest<String>(request.getEntity().getName(), request);
        mediaStoreRequestValidation.validateMediaStoreNameExists(entityRequestName, "basicNotNull");

        final MediaStoreUpdateRequest conceptRequest = request.getEntity();

        MediaStoreEntity mediaStoreEntity = mediaStoreDAO.findByPK(request.getEntity().getUuid());
        mediaStoreEntity = mediaStoreMapper.updateRequestToEntity(conceptRequest, mediaStoreEntity);
        mediaStoreEntity.setModified(LocalDateTime.now());
        mediaStoreEntity.setModifiedBy(request.getUserId());

        EntityRequest<Long> mediaEntityRequest = new EntityRequest<Long>(request.getEntity().getMediaId(), request);
        mediaStoreRequestValidation.validateMediaIdExists(mediaEntityRequest, "basicNotNull");
        MediaEntity media = mediaDAO.findByPK(request.getEntity().getMediaId());
        mediaStoreEntity.setMedia(media);

        mediaStoreDAO.merge(mediaStoreEntity);

        final MediaStoreResponse mediaStoreResponse = mediaStoreMapper.entityToResponse(mediaStoreEntity);

        return new PayloadResponse<MediaStoreResponse>(request, ResponseCode.OK, mediaStoreResponse);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> delete(final EntityRequest<String> request) throws ApiException {
        EntityRequest<String> entityRequest = new EntityRequest<String>(request.getEntity(), request);
        mediaStoreRequestValidation.validateMediaStoreExists(entityRequest, "validateAbstractRequest");

        mediaStoreDAO.removeByPK(request.getEntity());

        return new PayloadResponse<String>(request, ResponseCode.OK, "Concept deleted!");
    }
}
