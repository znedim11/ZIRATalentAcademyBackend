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
import ba.com.zira.praksa.api.MediaService;
import ba.com.zira.praksa.api.model.media.Media;
import ba.com.zira.praksa.dao.MediaDAO;
import ba.com.zira.praksa.dao.model.MediaEntity;
import ba.com.zira.praksa.mapper.MediaMapper;

@Service
public class MediaServiceImpl implements MediaService {

    private RequestValidator requestValidator;
    private MediaDAO mediaDAO;
    private MediaMapper mediaMapper;

    public MediaServiceImpl(final RequestValidator requestValidator, MediaDAO mediaDAO, MediaMapper mediaMapper) {
        this.requestValidator = requestValidator;
        this.mediaDAO = mediaDAO;
        this.mediaMapper = mediaMapper;
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
