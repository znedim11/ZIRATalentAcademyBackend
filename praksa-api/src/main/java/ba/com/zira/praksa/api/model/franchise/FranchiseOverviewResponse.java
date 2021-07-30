package ba.com.zira.praksa.api.model.franchise;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import ba.com.zira.praksa.api.model.game.GameFranchiseResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FranchiseOverviewResponse implements Serializable {
    static final long serialVersionUID = 1L;
    private Long franchiseId;
    private String franchiseName;
    private LocalDateTime created;
    private String createdBy;
    private LocalDateTime startDate;
    private LocalDateTime lastReleaseDate;
    private String information;
    private String outineText;
    private String aliases;
    private Long numberOfGames;
    private Long numberOfPlatforms;
    private Long numberOfDevelopers;
    private Long numberOfPublishers;
    private Long numberOfReleases;
    private List<GameFranchiseResponse> games;
}