package ba.com.zira.praksa.api.model.media;

import java.io.Serializable;

import lombok.Data;

@Data
public class CoverImageHelper implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long objectId;
    private String objectType;
    private String url;

    public CoverImageHelper(Long objectId, String objectType, String url) {
        super();
        this.objectId = objectId;
        this.objectType = objectType;
        this.url = url;
    }

}
