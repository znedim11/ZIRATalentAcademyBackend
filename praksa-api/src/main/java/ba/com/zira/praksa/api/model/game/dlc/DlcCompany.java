package ba.com.zira.praksa.api.model.game.dlc;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DlcCompany implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long companyId;
    private String companyName;
    private Long numOfDlcsAsPublisher;
    private Long numOfDlcsAsDeveloper;
    private LocalDateTime firstReleaseDate;

    public DlcCompany() {

    }

    public DlcCompany(Long companyId, String companyName, Long numOfDlcsAsPublisher, Long numOfDlcsAsDeveloper,
            LocalDateTime firstReleaseDate) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.numOfDlcsAsPublisher = numOfDlcsAsPublisher;
        this.numOfDlcsAsDeveloper = numOfDlcsAsDeveloper;
        this.firstReleaseDate = firstReleaseDate;
    }
}