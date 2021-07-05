package ba.com.zira.praksa.api.model.review;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReviewSearchRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long gameId;
    private Long platformId;
    private String reviewerId;
    private Double lowestRating;
    private Double highestRating;
    private String type;
}
