package ba.com.zira.praksa.api.model.feature;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FeatureResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String description;

    private String type;

    private LocalDateTime created;

    private String createdBy;

    private LocalDateTime modified;

    private String modifiedBy;

}
