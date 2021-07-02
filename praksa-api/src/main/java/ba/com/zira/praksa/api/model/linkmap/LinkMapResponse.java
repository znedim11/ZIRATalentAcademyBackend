package ba.com.zira.praksa.api.model.linkmap;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LinkMapResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uuid;
    private String typeObjectA;
    private Long typeObjectAId;
    private String typeObjectB;
    private Long typeObjectBId;
    private LocalDateTime created;
    private String createdBy;
    private LocalDateTime modified;
    private String modifiedBy;

}
