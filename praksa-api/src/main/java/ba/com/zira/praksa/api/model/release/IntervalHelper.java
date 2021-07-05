package ba.com.zira.praksa.api.model.release;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class IntervalHelper implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime startOfSegment;
    private LocalDateTime endOfSegment;
    private Long releaseCount;

    public IntervalHelper(LocalDateTime startOfSegment, LocalDateTime endOfSegment) {
        this.startOfSegment = startOfSegment;
        this.endOfSegment = endOfSegment;
    }

}
