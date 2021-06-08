package ba.com.zira.praksa.api.model.concept;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ConceptRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private Long id;

    private String aliases;

    private LocalDateTime created;

    private String createdBy;

    private String information;

    private LocalDateTime modified;

    private String modifiedBy;

    private String name;

    // TODO: Add connection to LinkMap
}
