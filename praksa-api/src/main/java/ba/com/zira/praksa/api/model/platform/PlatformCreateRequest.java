package ba.com.zira.praksa.api.model.platform;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlatformCreateRequest implements Serializable {

    static final long serialVersionUID = 1L;
    String abbriviation;
    String code;
    LocalDateTime created;
    String createdBy;
    String fullName;
    String information;
    String outlineText;
    private List<Long> releaseIds;

}
