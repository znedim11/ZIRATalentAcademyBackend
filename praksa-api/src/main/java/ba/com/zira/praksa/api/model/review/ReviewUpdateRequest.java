package ba.com.zira.praksa.api.model.review;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ReviewUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private Long id;

    private String title;

    private String text;

    private Long formulaId;

    private Map<String, Double> grades;

    private Double totalRating;
}
