package ba.com.zira.praksa.api.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class SampleModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String documentName;
}