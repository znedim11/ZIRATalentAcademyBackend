package ba.com.zira.praksa.api.model.region;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Region implements Serializable {

    static final long serialVersionUID = 1L;

    Long id;
    String name;
    String code;
    LocalDateTime releaseDate;

}
