package ba.com.zira.praksa.api.model.media;

import java.io.Serializable;
import lombok.Data;

@Data
public class MediaRetrivalRequest implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String objectType;
    private Long objectId;
    private String mediaType;

}
