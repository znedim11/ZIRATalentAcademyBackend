package ba.com.zira.praksa.api.model.person;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String aliases;
    private String city;
    private String country;
    private LocalDateTime created;
    private String createdBy;
    private LocalDateTime dob;
    private LocalDateTime dod;
    private String email;
    private String firstName;
    private String gender;
    private String information;
    private String lastName;
    private LocalDateTime modified;
    private String modifiedBy;
    private String twitter;
    private String website;
}
