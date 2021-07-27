package ba.com.zira.praksa.api.model.game;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class GameFranchiseResponse implements Serializable {
    static final long serialVersionUID = 1L;
    private Long gameId;
    private String gameName;
    private LocalDateTime firstReleaseDate;
    private String imageUrl;
    private List<String> platforms;
    private List<String> developers;
    private List<String> publishers;
    private List<String> releases;
}
