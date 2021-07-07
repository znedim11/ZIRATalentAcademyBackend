package ba.com.zira.praksa.api.model.game;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GameCharacterResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long gameId;
    private String gameName;
    private LocalDateTime releaseDate;
    private String platformCode;

    public GameCharacterResponse() {

    }

    public GameCharacterResponse(Long gameId, String gameName, String platformCode, LocalDateTime releaseDate) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.releaseDate = releaseDate;
        this.platformCode = platformCode;
    }
}
