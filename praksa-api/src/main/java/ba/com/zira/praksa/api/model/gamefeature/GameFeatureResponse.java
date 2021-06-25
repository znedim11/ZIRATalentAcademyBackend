package ba.com.zira.praksa.api.model.gamefeature;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GameFeatureResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uuid;

    private Long featureId;

    private Long gameId;

    private LocalDateTime created;

    private String createdBy;

    private LocalDateTime modified;

    private String modifiedBy;
}
