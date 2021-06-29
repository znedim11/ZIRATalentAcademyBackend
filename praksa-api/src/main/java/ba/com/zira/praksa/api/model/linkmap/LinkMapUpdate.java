package ba.com.zira.praksa.api.model.linkmap;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LinkMapUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uuid;

    private String ObjectAType;
    private long ObjectAId;

    private String ObjectBType;
    private long ObjectBId;

    private LocalDateTime created;

    private String created_by;

    private LocalDateTime modified;

    private String modified_by;

}
