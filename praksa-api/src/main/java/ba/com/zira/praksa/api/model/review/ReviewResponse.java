package ba.com.zira.praksa.api.model.review;

import java.io.Serializable;

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

    public ReviewResponse() {
    }

    public ReviewResponse(final String game, final Long gameId, final String platformName, final Long platformId, final String title,
            final String reviewerName, final Double totalRating, final Long reviewId) {
        super();
        this.gameId = gameId;
        this.gameName = game;
        this.platform = platformName;
        this.platformId = platformId;
        this.title = title;
        this.id = reviewId;
        this.reviewerName = reviewerName;
        this.totalRating = totalRating;
    }
}
