package ba.com.zira.praksa.api.model.media;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateMediaRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String objectType;
    private Long objectId;
    private String mediaObjectData;
    private String mediaObjectName;
    private String mediaObjectType;
    private String mediaStoreType;
}