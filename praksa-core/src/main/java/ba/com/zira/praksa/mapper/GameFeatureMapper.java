package ba.com.zira.praksa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ba.com.zira.praksa.api.model.gamefeature.GameFeatureCreateRequest;
import ba.com.zira.praksa.api.model.gamefeature.GameFeatureResponse;
import ba.com.zira.praksa.api.model.gamefeature.GameFeatureUpdateRequest;
import ba.com.zira.praksa.dao.model.GameFeatureEntity;

/**
 * Defined mapper interface for mapping a DTO to Entity model class <br>
 * for GameFeature
 *
 * @author zira
 *
 */
@Mapper(componentModel = "spring")
public interface GameFeatureMapper {

    GameFeatureResponse entityToDto(GameFeatureEntity gameFeatureDto);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateDtoToEntity(GameFeatureUpdateRequest gameFeatureDto, @MappingTarget GameFeatureEntity gameFeatureEntity);

    GameFeatureEntity dtoToEntity(GameFeatureCreateRequest gameFeatureDto);
}