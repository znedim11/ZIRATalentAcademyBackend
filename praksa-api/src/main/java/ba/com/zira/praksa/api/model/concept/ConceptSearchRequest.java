package ba.com.zira.praksa.api.model.concept;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ConceptSearchRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private String sortBy;

    private List<Long> gameIds;

    private List<Long> characterIds;
}
