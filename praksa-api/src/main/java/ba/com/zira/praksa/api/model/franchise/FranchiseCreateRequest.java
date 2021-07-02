package ba.com.zira.praksa.api.model.franchise;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FranchiseCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private String aliases;
    private LocalDateTime created;
    private String createdBy;
    private String information;
    private String name;
    private String outlineText;

}