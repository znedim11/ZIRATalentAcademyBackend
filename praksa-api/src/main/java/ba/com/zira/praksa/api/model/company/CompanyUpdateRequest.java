package ba.com.zira.praksa.api.model.company;

import java.io.Serializable;
import java.time.LocalDateTime;

import ba.com.zira.praksa.api.model.utils.ImageCreateRequest;
import lombok.Data;

@Data
public class CompanyUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String address;
    private String city;
    private String country;
    private String email;
    private LocalDateTime endDate;
    private String information;
    private String name;
    private String outlineText;
    private LocalDateTime startDate;
    private String telNumber;
    private String website;

    private ImageCreateRequest imageCreateRequest;
}
