package ba.com.zira.praksa.api.model.platform;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlatformUpdateRequest implements Serializable {

    static final long serialVersionUID = 1L;
    Long id;
    String abbriviation;
    String code;
    String fullName;
    String information;
    LocalDateTime modified;
    String modifiedBy;
    String outlineText;

}
