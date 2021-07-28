package ba.com.zira.praksa.api.model.company.report;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import lombok.Data;

@Data
public class CompanyRegionPlatformResponseDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long companyId;
    private String companyName;
    private LocalDateTime startDate;
    private Map<Long, CompanyRegionPlatform> regionMap;
    private Map<Long, CompanyRegionPlatform> platformMap;

    public CompanyRegionPlatformResponseDetail() {

    }

    public CompanyRegionPlatformResponseDetail(Long companyId, String companyName, LocalDateTime startDate,
            Map<Long, CompanyRegionPlatform> regionMap, Map<Long, CompanyRegionPlatform> platformMap) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.startDate = startDate;
        this.regionMap = regionMap;
        this.platformMap = platformMap;
    }
}