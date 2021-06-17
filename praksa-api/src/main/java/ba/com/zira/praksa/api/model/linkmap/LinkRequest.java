package ba.com.zira.praksa.api.model.linkmap;

import java.io.Serializable;

import lombok.Data;

@Data
public class LinkRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String objectAType;
    private Long objectAId;

    private String objectBType;
    private Long objectBId;
}
