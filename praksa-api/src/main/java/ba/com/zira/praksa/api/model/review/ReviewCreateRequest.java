package ba.com.zira.praksa.api.model.review;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

@Data
public class ReviewCreateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;

    private String text;

    private Long formulaId;

    private Long gameId;

    private Map<String, Double> grades;

    private Double totalRating;
}
