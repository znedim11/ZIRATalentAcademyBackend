package ba.com.zira.praksa.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ba.com.zira.praksa.api.model.region.RegionCreateRequest;
import ba.com.zira.praksa.api.model.region.RegionResponse;
import ba.com.zira.praksa.api.model.region.RegionUpdateRequest;
import ba.com.zira.praksa.dao.model.RegionEntity;

@Mapper(componentModel = "spring")
public interface RegionMapper {

    @Mapping(source = "name", target = "name")
    RegionResponse regionEntityToRegion(RegionEntity sampleModelEntity);

    @Mapping(source = "name", target = "name")
    RegionEntity regionToRegionEntity(RegionCreateRequest sampleModel);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateForRegionUpdate(RegionUpdateRequest regionModel, @MappingTarget RegionEntity regionEntity);

    RegionEntity dtoToEntity(RegionCreateRequest sample);

    @InheritInverseConfiguration(name = "dtoToEntity")
    RegionResponse entityToDto(RegionEntity billingPeriodEntity);

}
