package ba.com.zira.praksa.api.model.character;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import ba.com.zira.praksa.api.model.game.GameCharacterResponse;
import lombok.Data;

@Data
public class CompleteCharacterResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String realName;
    private String aliases;
    private String gender;
    private LocalDateTime dob;
    private LocalDateTime dod;
    private String outlineText;
    private String information;
    private String imageUrl;
    private List<GameCharacterResponse> games;

}