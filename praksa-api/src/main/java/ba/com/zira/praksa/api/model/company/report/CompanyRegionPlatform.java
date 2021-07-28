package ba.com.zira.praksa.api.model.company.report;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class CompanyRegionPlatform implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private Long numOfReleases;
    private LocalDateTime firstRelease;
    private String firstReleaseGameName;
    @JsonIgnore
    private Long companyId;
    @JsonIgnore
    private Long objId; // RegionId or PlatformId

    public CompanyRegionPlatform() {

    }

    public CompanyRegionPlatform(Long numOfReleases, LocalDateTime firstRelease, String firstReleaseGameName, Long companyId, Long objId,
            String name) {
        this.numOfReleases = numOfReleases;
        this.firstRelease = firstRelease;
        this.firstReleaseGameName = firstReleaseGameName;
        this.companyId = companyId;
        this.objId = objId;
        this.name = name;
    }
}