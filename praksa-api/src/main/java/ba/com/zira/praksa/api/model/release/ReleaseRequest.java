package ba.com.zira.praksa.api.model.release;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReleaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDateTime releaseDate;
    private String type;
    private Long developerId;
    private String developerName;
    private Long publisherId;
    private String publisherName;
    private Long gameId;
    private String gameName;
    private Long platformId;
    private String platformName;
    private Long regionId;
    private String regionName;

}
