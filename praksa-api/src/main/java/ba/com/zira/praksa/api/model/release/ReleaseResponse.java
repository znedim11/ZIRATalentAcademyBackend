package ba.com.zira.praksa.api.model.release;

import java.io.Serializable;
import java.time.LocalDateTime;

import ba.com.zira.praksa.api.model.company.CompanyResponse;
import ba.com.zira.praksa.api.model.game.GameResponse;
import ba.com.zira.praksa.api.model.platform.PlatformResponse;
import ba.com.zira.praksa.api.model.region.RegionResponse;
import lombok.Data;

@Data
public class ReleaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private String uuid;
    private LocalDateTime created;
    private String createdBy;
    private LocalDateTime modified;
    private String modifiedBy;
    private LocalDateTime releaseDate;
    private String type;
    private CompanyResponse developer;
    private CompanyResponse publisher;
    private GameResponse game;
    private PlatformResponse platform;
    private RegionResponse region;
}
