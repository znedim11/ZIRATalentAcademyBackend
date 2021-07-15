package ba.com.zira.praksa.api.model.game.dlc;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DlcFranchise implements Serializable {
    private static final long serialVersionUID = 1L;

    Long franchiseId;
    String franchiseName;
    Long numOfDlcs;
    LocalDateTime firstReleaseDate;

    public DlcFranchise() {

    }

    public DlcFranchise(Long franchiseId, String franchiseName, Long numOfDlcs, LocalDateTime firstReleaseDate) {
        this.franchiseId = franchiseId;
        this.franchiseName = franchiseName;
        this.numOfDlcs = numOfDlcs;
        this.firstReleaseDate = firstReleaseDate;
    }
}