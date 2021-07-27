package ba.com.zira.praksa.api.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class WikiStatsResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    String type;
    Long amount;

    public WikiStatsResponse(String type, Long amount) {
        super();
        this.type = type;
        this.amount = amount;
    }

}
