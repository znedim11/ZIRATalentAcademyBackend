package ba.com.zira.praksa.api.model.review;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ba.com.zira.praksa.api.model.reviewgrade.ReviewGradeCreateRequest;
import lombok.Data;

@Data
public class ReviewUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private Long id;

    private String title;

    private String text;

    private Long formulaId;

    private List<ReviewGradeCreateRequest> grades;

    private Double totalRating;
}
