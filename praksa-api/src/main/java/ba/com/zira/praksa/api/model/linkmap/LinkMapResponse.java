package ba.com.zira.praksa.api.model.linkmap;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LinkMapResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uuid;

    private String TypeObjectA;
    private long TypeObjectAId;

    private String TypeObjectB;
    private long TypeObjectBId;

    private LocalDateTime created;

    private String created_by;

    private LocalDateTime modified;

    private String modified_by;

}
