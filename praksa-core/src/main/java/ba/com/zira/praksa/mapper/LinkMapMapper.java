package ba.com.zira.praksa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ba.com.zira.praksa.api.model.linkmap.LinkMapCreate;
import ba.com.zira.praksa.api.model.linkmap.LinkMapResponse;
import ba.com.zira.praksa.api.model.linkmap.LinkMapUpdate;
import ba.com.zira.praksa.dao.model.LinkMapEntity;

@Mapper(componentModel = "spring")
public interface LinkMapMapper {

    LinkMapEntity dtoToEntity(LinkMapResponse linkMapResopnse);

    LinkMapResponse entityToDto(LinkMapEntity linkMapEntity);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateRequestToEntity(LinkMapUpdate conceptUpdateRequest, @MappingTarget LinkMapEntity linkMapEntity);

    LinkMapEntity createRequestToEntity(LinkMapCreate linkMapCreateRequest);

    List<LinkMapResponse> entityListToResponseList(List<LinkMapEntity> linkMapEntityList);
}
