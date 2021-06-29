package ba.com.zira.praksa.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ba.com.zira.praksa.api.model.release.ReleaseRequest;
import ba.com.zira.praksa.api.model.release.ReleaseResponseLight;
import ba.com.zira.praksa.dao.model.ReleaseEntity;

@Mapper(componentModel = "spring")
public interface ReleaseMapper {

    @Mapping(source = "type", target = "type")
    ReleaseResponseLight releaseEntityToRelease(ReleaseEntity sampleModelEntity);

    @Mapping(source = "type", target = "type")
    ReleaseEntity releaseToReleaseEntity(ReleaseRequest sampleModel);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateForReleaseUpdate(ReleaseRequest releaseModel, @MappingTarget ReleaseEntity releaseEntity);

    ReleaseEntity dtoToEntity(ReleaseRequest sample);

    @InheritInverseConfiguration(name = "dtoToEntity")
    ReleaseResponseLight entityToDto(ReleaseEntity billingPeriodEntity);
}
