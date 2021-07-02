package ba.com.zira.praksa.core.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import ba.com.zira.praksa.api.FileUploadService;
import ba.com.zira.praksa.api.MediaService;
import ba.com.zira.praksa.api.model.media.CreateMediaRequest;
import ba.com.zira.praksa.api.model.media.Media;
import ba.com.zira.praksa.api.model.utils.ImageUploadRequest;
import ba.com.zira.praksa.dao.MediaDAO;
import ba.com.zira.praksa.dao.MediaStoreDAO;
import ba.com.zira.praksa.dao.model.MediaEntity;
import ba.com.zira.praksa.dao.model.MediaStoreEntity;
import ba.com.zira.praksa.mapper.MediaMapper;

@Service
public class MediaServiceImpl implements MediaService {

    private RequestValidator requestValidator;
    private MediaDAO mediaDAO;
    private MediaMapper mediaMapper;
    private FileUploadService fileUploadService;
    private MediaStoreDAO mediaStoreDAO;

    public MediaServiceImpl(RequestValidator requestValidator, MediaDAO mediaDAO, MediaMapper mediaMapper,
            FileUploadService fileUploadService, MediaStoreDAO mediaStoreDAO) {
        super();
        this.requestValidator = requestValidator;
        this.mediaDAO = mediaDAO;
        this.mediaMapper = mediaMapper;
        this.fileUploadService = fileUploadService;
        this.mediaStoreDAO = mediaStoreDAO;
    }

    @Override
    public PayloadResponse<String> saveMedia(EntityRequest<CreateMediaRequest> request) throws ApiException {
        if ("IMAGE".equalsIgnoreCase(request.getEntity().getMediaObjectType())) {
            ImageUploadRequest imageUploadRequest = new ImageUploadRequest();
            imageUploadRequest.setImageData(request.getEntity().getMediaObjectData());
            imageUploadRequest.setImageName(request.getEntity().getMediaObjectName());
            EntityRequest<ImageUploadRequest> uploadRequest = new EntityRequest<>();
            uploadRequest.setEntity(imageUploadRequest);
            Map<String, String> imageInfo = fileUploadService.uploadImage(uploadRequest);
            if (imageInfo != null) {
                MediaEntity mediaEntity = new MediaEntity();
                mediaEntity.setCreated(LocalDateTime.now());
                mediaEntity.setCreatedBy(request.getUserId());
                mediaEntity.setObjectId(request.getEntity().getObjectId());
                mediaEntity.setObjectType(request.getEntity().getObjectType());
                MediaEntity createdMediaEntity = mediaDAO.persist(mediaEntity);
                MediaStoreEntity mediaStoreEntity = new MediaStoreEntity();
                mediaStoreEntity.setCreated(LocalDateTime.now());
                mediaStoreEntity.setCreatedBy(request.getUserId());
                mediaStoreEntity.setName(imageInfo.get("baseName"));
                mediaStoreEntity.setExtension(imageInfo.get("extension"));
                mediaStoreEntity.setType(request.getEntity().getMediaStoreType());
                mediaStoreEntity.setUrl(imageInfo.get("url"));
                mediaStoreEntity.setMedia(createdMediaEntity);
                mediaStoreDAO.persist(mediaStoreEntity);
            }
        }
        return new PayloadResponse<>(request, ResponseCode.OK, "Media Saved!");

    }

    @Override
    public PagedPayloadResponse<Media> find(final SearchRequest<String> request) throws ApiException {
        requestValidator.validate(request);

        PagedData<MediaEntity> mediaModelEntities = mediaDAO.findAll(request.getFilter());
        final List<Media> mediaList = new ArrayList<>();

        for (final MediaEntity MediaEntity : mediaModelEntities.getRecords()) {
            mediaList.add(mediaMapper.entityToDto(MediaEntity));
        }
        return new PagedPayloadResponse<>(request, ResponseCode.OK, mediaList.size(), 1, 1, mediaList.size(), mediaList);
    }

    @Override
    public PayloadResponse<Media> create(EntityRequest<Media> request) throws ApiException {
        requestValidator.validate(request);
        MediaEntity entity = mediaMapper.dtoToEntity(request.getEntity());
        mediaDAO.persist(entity);
        Media response = mediaMapper.entityToDto(entity);
        return new PayloadResponse<>(request, ResponseCode.OK, response);
    }

    @Override
    public PayloadResponse<Media> findById(final SearchRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        final MediaEntity mediaEntity = mediaDAO.findByPK(request.getEntity());

        final Media media = mediaMapper.entityToDto(mediaEntity);
        return new PayloadResponse<>(request, ResponseCode.OK, media);
    }

    @Transactional(rollbackFor = ApiException.class)
    @Override
    public PayloadResponse<Media> update(final EntityRequest<Media> request) throws ApiException {
        requestValidator.validate(request);

        final Media media = request.getEntity();
        final MediaEntity mediaEntity = mediaMapper.dtoToEntity(media);

        mediaDAO.merge(mediaEntity);

        return new PayloadResponse<>(request, ResponseCode.OK, media);

    }

    @Override
    public PayloadResponse<String> delete(final EntityRequest<Long> request) throws ApiException {
        requestValidator.validate(request);

        mediaDAO.removeByPK(request.getEntity());
        return new PayloadResponse<>(request, ResponseCode.OK, "Media deleted!");
    }

}
