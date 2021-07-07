package ba.com.zira.praksa.api.model.release;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class IntervalHelper implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime startOfSegment;
    private LocalDateTime endOfSegment;
    private Long releaseCount;
    private transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

    public IntervalHelper(LocalDateTime startOfSegment, LocalDateTime endOfSegment) {
        this.startOfSegment = startOfSegment;
        this.endOfSegment = endOfSegment;
    }

    @Override
    public String toString() {
        return formatter.format(startOfSegment) + " - " + formatter.format(endOfSegment) + "( " + releaseCount + " release/s )";

    }

}
