package ba.com.zira.praksa.api.model.franchise;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class FranchiseCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private String aliases;
    private String information;
    private String name;
    private String outlineText;
    private List<Long> gamesIds;

}