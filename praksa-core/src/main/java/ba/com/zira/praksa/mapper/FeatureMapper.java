package ba.com.zira.praksa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import ba.com.zira.praksa.api.model.feature.Feature;
import ba.com.zira.praksa.api.model.feature.FeatureCreateRequest;
import ba.com.zira.praksa.dao.model.FeatureEntity;

/**
 *
 * @author Ajas
 *
 */
@Mapper(componentModel = "spring")
public interface FeatureMapper {

    FeatureMapper INSTANCE = Mappers.getMapper(FeatureMapper.class);

    FeatureEntity dtoToEntity(Feature featureDto);

    FeatureEntity dtoToEntity(FeatureCreateRequest featureDto);

    // Ignoring unnecessary non-null fields to avoid exceptions
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateDtoToEntity(Feature featureDto, @MappingTarget FeatureEntity featureEntity);

    Feature entityToDto(FeatureEntity featureEntity);
}
