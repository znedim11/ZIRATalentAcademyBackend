package ba.com.zira.praksa.api.model.gamefeature;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class GameFeatureUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private String uuid;

    private Long featureId;

    private Long gameId;
}
