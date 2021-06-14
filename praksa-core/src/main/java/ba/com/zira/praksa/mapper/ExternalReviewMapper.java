package ba.com.zira.praksa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import ba.com.zira.praksa.api.model.externalReview.ExternalReview;
import ba.com.zira.praksa.api.model.externalReview.ExternalReviewCreateRequest;
import ba.com.zira.praksa.api.model.externalReview.ExternalReviewUpdateRequest;
import ba.com.zira.praksa.dao.model.ExternalReviewEntity;

@Mapper(componentModel = "spring")
public interface ExternalReviewMapper {

    ExternalReviewMapper INSTANCE = Mappers.getMapper(ExternalReviewMapper.class);

    ExternalReviewEntity dtoToEntity(ExternalReview sample);

    ExternalReview entityToDto(ExternalReviewEntity externalReviewEntity);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    ExternalReviewEntity updateRequestToEntity(ExternalReviewUpdateRequest externalReviewUpdateRequest,
            @MappingTarget ExternalReviewEntity externalReviewEntity);

    ExternalReviewEntity createRequestToEntity(ExternalReviewCreateRequest conceptCreateRequest);

    List<ExternalReview> entityListToExternalReviewList(List<ExternalReviewEntity> externalReviewEntityList);
}