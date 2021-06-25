package ba.com.zira.praksa.api.model.company;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CompanyResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String address;
    private String city;
    private String country;
    private LocalDateTime created;
    private String createdBy;
    private String email;
    private LocalDateTime endDate;
    private String information;
    private LocalDateTime modified;
    private String modifiedBy;
    private String name;
    private String outlineText;
    private LocalDateTime startDate;
    private String telNumber;
    private String website;  
}

