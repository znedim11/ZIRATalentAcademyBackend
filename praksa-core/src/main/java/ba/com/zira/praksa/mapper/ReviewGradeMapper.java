package ba.com.zira.praksa.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import ba.com.zira.praksa.api.model.reviewgrade.ReviewGradeCreateRequest;
import ba.com.zira.praksa.api.model.reviewgrade.ReviewGradeResponse;
import ba.com.zira.praksa.dao.model.ReviewGradeEntity;

@Mapper(componentModel = "spring")
public interface ReviewGradeMapper {

    ReviewGradeEntity reviewGradeCreateRequestToEntity(ReviewGradeCreateRequest createRequest);

    ReviewGradeResponse reviewGradeEntityToResponse(ReviewGradeEntity entity);

    List<ReviewGradeResponse> reviewGradeEntityListToResponseList(List<ReviewGradeEntity> entity);

    ReviewGradeResponse reviewGradeCreateRequestToResponse(ReviewGradeCreateRequest createRequest);

    List<ReviewGradeResponse> reviewGradeCreateRequestListToResponseList(List<ReviewGradeCreateRequest> createRequest);

    ReviewGradeEntity reviewGradeResponseToEntity(ReviewGradeResponse response);
}
