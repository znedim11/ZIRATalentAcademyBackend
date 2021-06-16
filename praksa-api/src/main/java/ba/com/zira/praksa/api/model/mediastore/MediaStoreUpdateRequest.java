package ba.com.zira.praksa.api.model.mediastore;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class MediaStoreUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private String uuid;
    private String name;
    private String type;
    private String url;
    private String extension;
    private Long mediaId;
}
