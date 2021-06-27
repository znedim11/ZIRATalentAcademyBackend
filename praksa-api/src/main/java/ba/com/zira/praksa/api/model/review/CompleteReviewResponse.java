package ba.com.zira.praksa.api.model.review;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class CompleteReviewResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long totalReviews;
    private String topPlatformName;
    private Double averageGrade;
    private String topGameName;
    private String flopGameName;
    private List<ReviewResponse> reviews;

    public CompleteReviewResponse() {
    }

    public CompleteReviewResponse(List<ReviewResponse> reviews) {
        this.reviews = reviews;
    }

    public CompleteReviewResponse(final String plaformName, final Long totalReviews) {
        this.totalReviews = totalReviews;
        this.topPlatformName = plaformName;
    }

    public CompleteReviewResponse(final String gameName, final String gameType) {
        if (gameType.equals("top")) {
            this.topGameName = gameName;
        } else if (gameType.equals("flop")) {
            this.flopGameName = gameName;
        }
    }

}
