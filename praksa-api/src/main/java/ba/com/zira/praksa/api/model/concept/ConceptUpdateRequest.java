package ba.com.zira.praksa.api.model.concept;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ConceptUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private Long id;

    private String aliases;

    private String information;

    private String name;
}
