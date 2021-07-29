package ba.com.zira.praksa.api.model.release;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReleaseResponseDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    private String uuid;
    private LocalDateTime releaseDate;
    private String type;
    private Long developerId;
    private Long publisherId;
    private String developerName;
    private String publisherName;
    private Long gameId;
    private String gameName;
    private Long platformId;
    private String platformName;
    private Long regionId;
    private String regionName;
    private String imageUrl;

}
