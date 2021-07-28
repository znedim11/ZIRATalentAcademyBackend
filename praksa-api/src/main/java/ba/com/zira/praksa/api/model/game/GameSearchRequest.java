package ba.com.zira.praksa.api.model.game;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class GameSearchRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String releasedBefore;
    private String releasedAfter;
    private String name;
    private String genre;
    private Long developerId;
    private Long publisherId;
    private List<Long> regionIds;
    private List<Long> featureIds;

    public GameSearchRequest(String releasedBefore, String releasedAfter, String name, String genre, Long developerId, Long publisherId,
            List<Long> regionIds, List<Long> featureIds) {
        super();
        this.releasedBefore = releasedBefore;
        this.releasedAfter = releasedAfter;
        this.name = name;
        this.genre = genre;
        this.developerId = developerId;
        this.publisherId = publisherId;
        this.regionIds = regionIds;
        this.featureIds = featureIds;
    }

}