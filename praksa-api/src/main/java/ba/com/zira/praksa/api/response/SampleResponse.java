package ba.com.zira.praksa.api.response;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SampleResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String documentName;
}
