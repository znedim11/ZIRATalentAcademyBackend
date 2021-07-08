package ba.com.zira.praksa.rest.review;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ba.com.zira.commons.exception.ApiException;
import ba.com.zira.commons.message.request.EntityRequest;
import ba.com.zira.commons.message.request.SearchRequest;
import ba.com.zira.commons.message.response.PagedPayloadResponse;
import ba.com.zira.commons.message.response.PayloadResponse;
import ba.com.zira.praksa.api.ReviewService;
import ba.com.zira.praksa.api.model.review.CompleteReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewCreateRequest;
import ba.com.zira.praksa.api.model.review.ReviewResponse;
import ba.com.zira.praksa.api.model.review.ReviewSearchRequest;
import ba.com.zira.praksa.api.model.review.ReviewUpdateRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "review")
@RestController
@RequestMapping(value = "review")
public class ReviewRestService {

    @Autowired
    ReviewService reviewService;

    @ApiOperation(value = "Search for a review", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/search")
    public PagedPayloadResponse<ReviewResponse> search(@RequestParam(required = false) Long gameId,
            @RequestParam(required = false) String reviewerId, @RequestParam(required = false) Long platformId,
            @RequestParam(required = false) Double lowestRating, @RequestParam(required = false) Double highestRating,
            @RequestParam(required = false) String type) throws ApiException {

        ReviewSearchRequest reviewRequest = new ReviewSearchRequest();
        reviewRequest.setGameId(gameId);
        reviewRequest.setPlatformId(platformId);
        reviewRequest.setReviewerId(reviewerId);
        reviewRequest.setLowestRating(lowestRating);
        reviewRequest.setHighestRating(highestRating);
        reviewRequest.setType(type);

        EntityRequest<ReviewSearchRequest> entityRequest = new EntityRequest<>();
        entityRequest.setEntity(reviewRequest);

        return reviewService.searchReviews(entityRequest);
    }

    @ApiOperation(value = "Get stats for reviews", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/stats")
    public PayloadResponse<CompleteReviewResponse> getStats(@RequestParam(required = false) Long gameId,
            @RequestParam(required = false) String reviewerId, @RequestParam(required = false) Long platformId,
            @RequestParam(required = false) Double lowestRating, @RequestParam(required = false) Double highestRating,
            @RequestParam(required = false) String type) throws ApiException {

        ReviewSearchRequest reviewRequest = new ReviewSearchRequest();
        reviewRequest.setGameId(gameId);
        reviewRequest.setPlatformId(platformId);
        reviewRequest.setReviewerId(reviewerId);
        reviewRequest.setLowestRating(lowestRating);
        reviewRequest.setHighestRating(highestRating);
        reviewRequest.setType(type);

        EntityRequest<ReviewSearchRequest> entityRequest = new EntityRequest<>();
        entityRequest.setEntity(reviewRequest);

        return reviewService.getStats(entityRequest);
    }

    @ApiOperation(value = "Create Review", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/create")
    public PayloadResponse<ReviewResponse> create(@RequestBody EntityRequest<ReviewCreateRequest> request) throws ApiException {
        return reviewService.create(request);
    }

    @ApiOperation(value = "Update Review", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public PayloadResponse<ReviewResponse> update(@PathVariable final Long id,
            @RequestBody final EntityRequest<ReviewUpdateRequest> request) throws ApiException {

        final ReviewUpdateRequest concept = request.getEntity();
        if (!Objects.isNull(concept)) {
            concept.setId(id);
        }

        return reviewService.update(request);
    }

    @ApiOperation(value = "Get Review by Id.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public PayloadResponse<ReviewResponse> findById(@PathVariable final Long id) throws ApiException {

        final SearchRequest<Long> request = new SearchRequest<>();
        request.setEntity(id);

        return reviewService.findById(request);
    }
}
