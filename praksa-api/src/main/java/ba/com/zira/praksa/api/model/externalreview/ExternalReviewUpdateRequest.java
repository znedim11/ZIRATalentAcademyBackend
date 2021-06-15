package ba.com.zira.praksa.api.model.externalreview;

import java.io.Serializable;

import lombok.Data;

@Data
public class ExternalReviewUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String information;
    private String origin;
    private Long rssFeedId;
}
