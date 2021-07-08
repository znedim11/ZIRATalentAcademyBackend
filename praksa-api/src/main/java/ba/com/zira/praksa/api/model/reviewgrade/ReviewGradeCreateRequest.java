package ba.com.zira.praksa.api.model.reviewgrade;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReviewGradeCreateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Double grade;

    private String type;
}
