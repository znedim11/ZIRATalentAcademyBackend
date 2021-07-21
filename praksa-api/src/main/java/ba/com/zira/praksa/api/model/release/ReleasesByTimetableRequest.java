package ba.com.zira.praksa.api.model.release;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReleasesByTimetableRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    LocalDateTime startDate;
    LocalDateTime endDate;
    String timeSegment;
    String releaseType;

}
