package ba.com.zira.praksa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ba.com.zira.commons.model.PagedData;
import ba.com.zira.praksa.api.model.feature.FeatureCreateRequest;
import ba.com.zira.praksa.api.model.feature.FeatureResponse;
import ba.com.zira.praksa.api.model.feature.FeatureUpdateRequest;
import ba.com.zira.praksa.dao.model.FeatureEntity;

/**
 * Defined mapper interface for mapping a DTO to Entity model class <br>
 * for Feature
 *
 * @author zira
 *
 */
@Mapper(componentModel = "spring")
public interface FeatureMapper {

    FeatureEntity dtoToEntity(FeatureResponse featureDto);

    FeatureEntity dtoToEntity(FeatureCreateRequest featureDto);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateDtoToEntity(FeatureUpdateRequest featureDto, @MappingTarget FeatureEntity featureEntity);

    FeatureResponse entityToDto(FeatureEntity featureEntity);

    PagedData<FeatureResponse> entitiesToDtos(PagedData<FeatureEntity> featureEntities);

    FeatureCreateRequest updateToCreateRequest(FeatureUpdateRequest update);
}
