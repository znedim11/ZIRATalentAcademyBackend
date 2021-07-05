package ba.com.zira.praksa.api.model.formula;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class FormulaCreateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private String formula;

    private List<String> grades;
}
