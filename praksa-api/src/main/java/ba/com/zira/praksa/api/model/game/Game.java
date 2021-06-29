package ba.com.zira.praksa.api.model.game;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String abbriviation;
    private LocalDateTime created;
    private String createdBy;
    private String dlc;
    private String fullName;
    private String genre;
    private String information;
    private LocalDateTime modified;
    private String modifiedBy;
    private String outlineText;
    private String imageUrl;
}
