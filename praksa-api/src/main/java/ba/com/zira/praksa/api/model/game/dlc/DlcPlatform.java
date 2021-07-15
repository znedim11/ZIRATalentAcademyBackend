package ba.com.zira.praksa.api.model.game.dlc;

import java.io.Serializable;

import lombok.Data;

@Data
public class DlcPlatform implements Serializable {
    private static final long serialVersionUID = 1L;

    Long platformId;
    String platformName;
    String platformCode;
    Long numOfDlcs;

    public DlcPlatform() {

    }

    public DlcPlatform(Long platformId, String platformName, String platformCode, Long numOfDlcs) {
        this.platformId = platformId;
        this.platformName = platformName;
        this.platformCode = platformCode;
        this.numOfDlcs = numOfDlcs;
    }
}