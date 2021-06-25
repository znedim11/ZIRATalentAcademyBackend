package ba.com.zira.praksa.api.model.gamefeature;

import java.io.Serializable;

import lombok.Data;

@Data
public class GameFeatureCreateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long featureId;

    private Long gameId;
}
