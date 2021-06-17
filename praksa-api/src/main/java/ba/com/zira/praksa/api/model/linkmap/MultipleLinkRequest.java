package ba.com.zira.praksa.api.model.linkmap;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

@Data
public class MultipleLinkRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String objectAType;
    private Long objectAId;

    private Map<String, Long> objectBMap;
}
