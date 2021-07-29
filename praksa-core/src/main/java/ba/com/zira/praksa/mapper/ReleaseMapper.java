package ba.com.zira.praksa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ba.com.zira.praksa.api.model.release.ReleaseRequest;
import ba.com.zira.praksa.api.model.release.ReleaseResponse;
import ba.com.zira.praksa.api.model.release.ReleaseResponseDetails;
import ba.com.zira.praksa.api.model.release.ReleaseResponseLight;
import ba.com.zira.praksa.dao.model.ReleaseEntity;

@Mapper(componentModel = "spring")
public interface ReleaseMapper {

    @Mapping(source = "type", target = "type")
    ReleaseResponseLight releaseEntityToRelease(ReleaseEntity sampleModelEntity);

    ReleaseResponse releaseEntityToResponse(ReleaseEntity entity);

    @Mapping(source = "platform.id", target = "platformId")
    @Mapping(source = "platform.fullName", target = "platformName")
    @Mapping(source = "game.id", target = "gameId")
    @Mapping(source = "game.fullName", target = "gameName")
    @Mapping(source = "publisher.id", target = "publisherId")
    @Mapping(source = "publisher.name", target = "publisherName")
    @Mapping(source = "developer.id", target = "developerId")
    @Mapping(source = "developer.name", target = "developerName")
    @Mapping(source = "region.name", target = "regionName")
    @Mapping(source = "region.id", target = "regionId")
    ReleaseResponseDetails entityToDetail(ReleaseEntity entity);

    @Mapping(source = "type", target = "type")
    ReleaseEntity releaseToReleaseEntity(ReleaseRequest sampleModel);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateForReleaseUpdate(ReleaseRequest releaseModel, @MappingTarget ReleaseEntity releaseEntity);

    ReleaseEntity dtoToEntity(ReleaseRequest sample);

    List<ReleaseResponseDetails> entityListToDtoList(List<ReleaseEntity> sampleList);

    List<ReleaseResponse> entityListToResponseList(List<ReleaseEntity> entityList);

    List<ReleaseResponse> entitiesToDtos(List<ReleaseEntity> rEntities);

}
