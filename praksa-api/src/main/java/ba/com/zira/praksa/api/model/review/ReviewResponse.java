package ba.com.zira.praksa.api.model.review;

import java.io.Serializable;
import java.util.List;

import ba.com.zira.praksa.api.model.reviewgrade.ReviewGradeResponse;
import lombok.Data;

@Data
public class ReviewResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long gameId;
    private String gameName;
    private String platform;
    private Long platformId;
    private String title;
    private String reviewerId;
    private String reviewerName;
    private Double totalRating;
    private String text;
    private Long formulaId;
    private String type;
    private Long numOfReviewesByReviewer;
    private List<ReviewGradeResponse> grades;

    public ReviewResponse() {
    }

    public ReviewResponse(final String game, final Long gameId, final String platformName, final Long platformId, final String title,
            final String reviewer, final Double totalRating, final Long reviewId, final String type) {
        super();
        this.gameId = gameId;
        this.gameName = game;
        this.platform = platformName;
        this.platformId = platformId;
        this.title = title;
        this.id = reviewId;
        this.reviewerId = reviewer;
        this.totalRating = totalRating;
        this.type = type;
    }
}
