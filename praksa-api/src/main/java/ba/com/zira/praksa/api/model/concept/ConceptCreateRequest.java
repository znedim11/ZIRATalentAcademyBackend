package ba.com.zira.praksa.api.model.concept;

import java.io.Serializable;

import lombok.Data;

@Data
public class ConceptCreateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String aliases;

    private String information;

    private String name;

    private String outline;
}
