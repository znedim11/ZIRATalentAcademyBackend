package ba.com.zira.praksa.api.model.externalReview;

import java.io.Serializable;

import lombok.Data;

@Data
public class ExternalReviewUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String information;

    private String origin;

    private Long rss_feed_id;
}
