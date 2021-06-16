package ba.com.zira.praksa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ba.com.zira.praksa.api.model.mediastore.MediaStoreCreateRequest;
import ba.com.zira.praksa.api.model.mediastore.MediaStoreResponse;
import ba.com.zira.praksa.api.model.mediastore.MediaStoreUpdateRequest;
import ba.com.zira.praksa.dao.model.MediaStoreEntity;

/**
 * Defined mapper interface for mapping a DTO to Entity model class
 *
 * @author zira
 *
 */
@Mapper(componentModel = "spring")
public interface MediaStoreMapper {

    MediaStoreEntity responseToEntity(MediaStoreResponse mediaStoreResponse);

    MediaStoreResponse entityToResponse(MediaStoreEntity mediaStoreEntity);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    MediaStoreEntity updateRequestToEntity(MediaStoreUpdateRequest mediaStoreUpdateRequest,
            @MappingTarget MediaStoreEntity mediaStoreEntity);

    MediaStoreEntity createRequestToEntity(MediaStoreCreateRequest mediaStoreCreateRequest);

    List<MediaStoreResponse> entityListToResponseList(List<MediaStoreEntity> mediaStoreEntityList);

}