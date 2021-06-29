package ba.com.zira.praksa.api.model.linkmap;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LinkMapCreate implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uuid;
    private String objectAType;
    private Long objectAId;
    private String objectBType;
    private Long objectBId;
    private LocalDateTime created;
    private String createdBy;
    private LocalDateTime modified;
    private String modifiedBy;
}
