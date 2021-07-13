package ba.com.zira.praksa.api.model.platform;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import ba.com.zira.praksa.api.model.game.GameDetailResponse;
import ba.com.zira.praksa.api.model.region.Region;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlatformOverviewResponse implements Serializable {

    static final long serialVersionUID = 1L;

    Long id;
    String abbriviation;
    String code;
    LocalDateTime created;
    String createdBy;
    String fullName;
    String information;
    String outlineText;
    Integer totalNumOfGames;
    String firstGameRelease;
    String latestGameRelease;

    private List<Region> regions;
    private List<GameDetailResponse> games;

}
