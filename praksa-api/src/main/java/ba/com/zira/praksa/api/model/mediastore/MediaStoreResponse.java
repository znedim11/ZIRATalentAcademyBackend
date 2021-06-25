package ba.com.zira.praksa.api.model.mediastore;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MediaStoreResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private String uuid;
    private LocalDateTime created;
    private String createdBy;
    private LocalDateTime modified;
    private String modifiedBy;
    private String name;
    private String type;
    private String url;
    private String extension;

}
