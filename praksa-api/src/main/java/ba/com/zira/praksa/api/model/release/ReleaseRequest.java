package ba.com.zira.praksa.api.model.release;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReleaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String releaseDate;
    private String type;
    private Long developerId;
    private Long publisherId;
    private Long gameId;
    private Long platformId;
    private Long regionId;

}
