package ba.com.zira.praksa.api.model.game;

import java.io.Serializable;

import ba.com.zira.praksa.api.model.utils.ImageCreateRequest;
import lombok.Data;

@Data
public class GameUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String abbriviation;
    private String dlc;
    private String fullName;
    private String genre;
    private String information;
    private String outlineText;
    private Long franchiseId;
    private Long dlcGameId;
    private ImageCreateRequest imageCreateRequest;
}