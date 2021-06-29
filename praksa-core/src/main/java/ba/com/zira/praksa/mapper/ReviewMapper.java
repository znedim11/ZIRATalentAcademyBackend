package ba.com.zira.praksa.mapper;

import org.mapstruct.Mapper;

import ba.com.zira.praksa.api.model.review.ReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewSearchRequest;
import ba.com.zira.praksa.dao.model.ReviewEntity;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewResponse reviewEntityToReview(ReviewEntity reviewEntity);

    ReviewEntity reviewToReviewEntity(ReviewSearchRequest review);

}
