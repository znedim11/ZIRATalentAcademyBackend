package ba.com.zira.praksa.api.model.review;

import java.io.Serializable;

import lombok.Data;

@Data
public class CompleteReviewResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long totalReviews;
    private String topPlatformName;
    private Long topPlatformId;
    private Double averageGrade;
    private String topGameName;
    private Long topGameId;
    private String flopGameName;
    private Long flopGameId;

    public CompleteReviewResponse() {
    }

    public CompleteReviewResponse(final String plaformName, final Long platformId, final Long totalReviews) {
        this.totalReviews = totalReviews;
        this.topPlatformName = plaformName;
        this.topPlatformId = platformId;
    }

    public CompleteReviewResponse(final String gameName, final Long gameId, final String gameType) {
        if (gameType.equals("top")) {
            this.topGameName = gameName;
            this.topGameId = gameId;
        } else if (gameType.equals("flop")) {
            this.flopGameName = gameName;
            this.flopGameId = gameId;
        }
    }

}
