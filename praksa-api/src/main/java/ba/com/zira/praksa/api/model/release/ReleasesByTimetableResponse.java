package ba.com.zira.praksa.api.model.release;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ReleasesByTimetableResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Map<String, List<ReleaseResponseDetails>> mapOfReleasesByIntervals;

}
