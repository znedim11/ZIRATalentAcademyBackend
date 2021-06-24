package ba.com.zira.praksa.api.model.person;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String firstName;
    private String lastName;
    private String information;
    private String gender;
    private LocalDateTime dob;
    private LocalDateTime dod;
    private String city;
    private String country;
    private String email;
    private String website;
    private String twitter;
    private String aliases;
    private LocalDateTime created;
    private String createdBy;
    private LocalDateTime modified;
    private String modifiedBy;
}
