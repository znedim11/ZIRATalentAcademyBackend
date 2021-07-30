package ba.com.zira.praksa.api.model.company;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CompanySearchRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private LocalDateTime dob;

    private String dobCondition;

    private String sortBy;

}
