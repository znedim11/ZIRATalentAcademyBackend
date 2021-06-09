package ba.com.zira.praksa.api.model.feature;

import java.io.Serializable;

import lombok.Data;

@Data
public class FeatureCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    private String type;
}
