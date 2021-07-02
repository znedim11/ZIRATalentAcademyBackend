package ba.com.zira.praksa.api.model.region;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RegionCreateRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDateTime created;
    private String createdBy;
    private String description;
    private String name;
}