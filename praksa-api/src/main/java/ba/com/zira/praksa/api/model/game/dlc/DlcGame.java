package ba.com.zira.praksa.api.model.game.dlc;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DlcGame implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long gameId;
    private String gameName;
    private Long numOfDlcs;
    private LocalDateTime firstReleaseDate;

    public DlcGame() {

    }

    public DlcGame(Long gameId, String gameName, Long numOfDlcs, LocalDateTime firstReleaseDate) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.numOfDlcs = numOfDlcs;
        this.firstReleaseDate = firstReleaseDate;
    }
}