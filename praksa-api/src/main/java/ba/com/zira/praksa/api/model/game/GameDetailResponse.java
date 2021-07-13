package ba.com.zira.praksa.api.model.game;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GameDetailResponse implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Long id;
    String name;
    Long publisherId;
    String publisherName;
    Long developerId;
    String developerName;
    LocalDateTime releaseDate;
}
