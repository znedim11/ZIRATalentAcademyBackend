package ba.com.zira.praksa.api.model.object;

import java.io.Serializable;

import lombok.Data;

@Data
public class ObjectUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String aliases;
    private String information;
    private String name;
}
