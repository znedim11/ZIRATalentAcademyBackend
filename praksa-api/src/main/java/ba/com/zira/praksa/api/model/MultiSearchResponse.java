package ba.com.zira.praksa.api.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class MultiSearchResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    Long objectId;
    String objectType;
    String objectName;
    String imageUrl;
    Long numberOfReleases;

    public MultiSearchResponse(Long objectId, String objectName, String objectType) {
        super();
        this.objectId = objectId;
        this.objectType = objectType;
        this.objectName = objectName;
        this.imageUrl = "";
        this.numberOfReleases = 0L;
    }

}
