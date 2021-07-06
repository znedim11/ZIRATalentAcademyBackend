package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
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
import ba.com.zira.praksa.api.model.media.MediaRetrivalRequest;

@Service
public class MediaStoreServiceImpl implements MediaStoreService {

    RequestValidator requestValidator;
    MediaStoreRequestValidation mediaStoreRequestValidation;
    MediaStoreDAO mediaStoreDAO;
    MediaDAO mediaDAO;
    MediaStoreMapper mediaStoreMapper;
    private static final String VALIDATE_ABSTRACT_REQUEST = "validateAbstractRequest";
    private static final String BASIC_NOT_NULL = "basicNotNull";

    public MediaStoreServiceImpl(RequestValidator requestValidator, MediaStoreRequestValidation mediaStoreRequestValidation,
            MediaStoreDAO mediaStoreDAO, MediaStoreMapper mediaStoreMapper, MediaDAO mediaDAO) {
        super();
        this.requestValidator = requestValidator;
        this.mediaStoreRequestValidation = mediaStoreRequestValidation;
        this.mediaStoreDAO = mediaStoreDAO;
        this.mediaStoreMapper = mediaStoreMapper;
        this.mediaDAO = mediaDAO;
    }

    @Override
    public PagedPayloadResponse<MediaStoreResponse> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<MediaStoreEntity> mediaStoreEntitesData = mediaStoreDAO.findAll(request.getFilter());
        List<MediaStoreEntity> mediaStoreEntities = mediaStoreEntitesData.getRecords();

        final List<MediaStoreResponse> mediaStoreList = mediaStoreMapper.entityListToResponseList(mediaStoreEntities);

        PagedData<MediaStoreResponse> pagedData = new PagedData<>();
        pagedData.setNumberOfPages(mediaStoreEntitesData.getNumberOfPages());
        pagedData.setNumberOfRecords(mediaStoreEntitesData.getNumberOfRecords());
        pagedData.setPage(mediaStoreEntitesData.getPage());
        pagedData.setRecords(mediaStoreList);
        pagedData.setRecordsPerPage(mediaStoreEntitesData.getRecordsPerPage());

        return new PagedPayloadResponse<>(request, ResponseCode.OK, pagedData);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<MediaStoreResponse> create(EntityRequest<MediaStoreCreateRequest> request) throws ApiException {
        mediaStoreRequestValidation.validateEntityExistsInRequest(request, BASIC_NOT_NULL);

        EntityRequest<String> entityRequest = new EntityRequest<>(request.getEntity().getName(), request);
        mediaStoreRequestValidation.validateMediaStoreNameExists(entityRequest, BASIC_NOT_NULL);

        MediaStoreEntity entity = mediaStoreMapper.createRequestToEntity(request.getEntity());
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(request.getUserId());

        UUID uuid = UUID.randomUUID();
        String uuidToString = uuid.toString();
        entity.setUuid(uuidToString);

        EntityRequest<Long> mediaEntityRequest = new EntityRequest<>(request.getEntity().getMediaId(), request);
        mediaStoreRequestValidation.validateMediaIdExists(mediaEntityRequest, BASIC_NOT_NULL);
        MediaEntity media = mediaDAO.findByPK(request.getEntity().getMediaId());
        entity.setMedia(media);

        mediaStoreDAO.merge(entity);

        MediaStoreResponse response = mediaStoreMapper.entityToResponse(entity);

        return new PayloadResponse<>(request, ResponseCode.OK, response);

    }

    @Override
    public PayloadResponse<MediaStoreResponse> findByUuid(final EntityRequest<String> request) throws ApiException {
        mediaStoreRequestValidation.validateMediaStoreExists(request, VALIDATE_ABSTRACT_REQUEST);

        final MediaStoreEntity mediaStoreEntity = mediaStoreDAO.findByPK(request.getEntity());

        final MediaStoreResponse mediaStoreResponse = mediaStoreMapper.entityToResponse(mediaStoreEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, mediaStoreResponse);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<MediaStoreResponse> update(final EntityRequest<MediaStoreUpdateRequest> request) throws ApiException {
        mediaStoreRequestValidation.validateEntityExistsInRequest(request, BASIC_NOT_NULL);

        EntityRequest<String> entityRequestUuid = new EntityRequest<>(request.getEntity().getUuid(), request);
        mediaStoreRequestValidation.validateMediaStoreExists(entityRequestUuid, VALIDATE_ABSTRACT_REQUEST);

        EntityRequest<String> entityRequestName = new EntityRequest<>(request.getEntity().getName(), request);
        mediaStoreRequestValidation.validateMediaStoreNameExists(entityRequestName, BASIC_NOT_NULL);

        final MediaStoreUpdateRequest conceptRequest = request.getEntity();

        MediaStoreEntity mediaStoreEntity = mediaStoreDAO.findByPK(request.getEntity().getUuid());
        mediaStoreEntity = mediaStoreMapper.updateRequestToEntity(conceptRequest, mediaStoreEntity);
        mediaStoreEntity.setModified(LocalDateTime.now());
        mediaStoreEntity.setModifiedBy(request.getUserId());

        EntityRequest<Long> mediaEntityRequest = new EntityRequest<>(request.getEntity().getMediaId(), request);
        mediaStoreRequestValidation.validateMediaIdExists(mediaEntityRequest, BASIC_NOT_NULL);
        MediaEntity media = mediaDAO.findByPK(request.getEntity().getMediaId());
        mediaStoreEntity.setMedia(media);

        mediaStoreDAO.merge(mediaStoreEntity);

        final MediaStoreResponse mediaStoreResponse = mediaStoreMapper.entityToResponse(mediaStoreEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, mediaStoreResponse);
    }

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public PayloadResponse<String> delete(final EntityRequest<String> request) throws ApiException {
        EntityRequest<String> entityRequest = new EntityRequest<>(request.getEntity(), request);
        mediaStoreRequestValidation.validateMediaStoreExists(entityRequest, VALIDATE_ABSTRACT_REQUEST);

        mediaStoreDAO.removeByPK(request.getEntity());

        return new PayloadResponse<>(request, ResponseCode.OK, "Concept deleted!");
    }

    @Override
    public ListPayloadResponse<String> getImageUrl(final EntityRequest<MediaRetrivalRequest> request) throws ApiException {
        mediaStoreRequestValidation.validateEntityExistsInRequest(request, VALIDATE_ABSTRACT_REQUEST);
        List<String> url = new ArrayList<>();
        url.addAll(mediaStoreDAO.getUrl(request.getEntity().getObjectId(), request.getEntity().getObjectType(),
                request.getEntity().getMediaType()));
        if (url.isEmpty()) {
            url.add("");
        }
        return new ListPayloadResponse<>(request, ResponseCode.OK, url);

    }
}
