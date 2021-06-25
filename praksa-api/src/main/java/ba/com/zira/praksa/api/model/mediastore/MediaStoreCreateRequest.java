package ba.com.zira.praksa.api.model.mediastore;

import java.io.Serializable;

import lombok.Data;

@Data
public class MediaStoreCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String type;
    private String url;
    private String extension;
    private Long mediaId;
}
