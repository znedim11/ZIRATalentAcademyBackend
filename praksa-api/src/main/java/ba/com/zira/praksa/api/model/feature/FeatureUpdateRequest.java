package ba.com.zira.praksa.api.model.feature;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class FeatureUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private Long id;

    private String name;

    private String description;

    private String type;
}
