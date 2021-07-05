package ba.com.zira.praksa.api.model.formula;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class FormulaUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private Long id;

    private String name;

    private String formula;

    private List<String> grades;
}
