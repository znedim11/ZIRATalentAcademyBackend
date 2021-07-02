package ba.com.zira.praksa.api.model.object;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ObjectResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String aliases;
    private LocalDateTime created;
    private String createdBy;
    private String information;
    private LocalDateTime modified;
    private String modifiedBy;
    private String name;
}