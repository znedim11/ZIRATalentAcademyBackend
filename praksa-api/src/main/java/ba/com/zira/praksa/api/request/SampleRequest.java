package ba.com.zira.praksa.api.request;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SampleRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String documentName;
    

}
