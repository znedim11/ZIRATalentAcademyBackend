package ba.com.zira.praksa.api.model.game.dlc;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DlcAnalysisReport implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long totalNumberOfDlc;
    private List<DlcPlatform> dlcPlatforms;
    private List<DlcGame> dlcGames;
    private List<DlcFranchise> dlcFranchises;
    private List<DlcCompany> dlcCompanies;
}
