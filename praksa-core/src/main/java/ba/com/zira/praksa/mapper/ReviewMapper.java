package ba.com.zira.praksa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ba.com.zira.praksa.api.model.review.ReviewCreateRequest;
import ba.com.zira.praksa.api.model.review.ReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewSearchRequest;
import ba.com.zira.praksa.api.model.review.ReviewUpdateRequest;
import ba.com.zira.praksa.dao.model.ReviewEntity;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "reviewEntity.game.id", target = "gameId")
    @Mapping(source = "reviewEntity.reviewFormula.id", target = "formulaId")
    ReviewResponse reviewEntityToReview(ReviewEntity reviewEntity);

    ReviewEntity reviewToReviewEntity(ReviewSearchRequest review);

    ReviewEntity createRequestToEntity(ReviewCreateRequest entity);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    ReviewEntity updateRequestToEntity(ReviewUpdateRequest reviewRequest, @MappingTarget ReviewEntity reviewEntity);
}
