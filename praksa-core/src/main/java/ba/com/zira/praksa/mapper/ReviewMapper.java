package ba.com.zira.praksa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ba.com.zira.praksa.api.model.review.ReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewSearchRequest;
import ba.com.zira.praksa.dao.model.ReviewEntity;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    ReviewResponse reviewEntityToReview(ReviewEntity reviewEntity);

    ReviewEntity reviewToReviewEntity(ReviewSearchRequest review);

}
