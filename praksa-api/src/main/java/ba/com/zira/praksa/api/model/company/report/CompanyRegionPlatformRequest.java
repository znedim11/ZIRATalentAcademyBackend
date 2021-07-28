package ba.com.zira.praksa.api.model.company.report;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class CompanyRegionPlatformRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Long> companyIds;
}
