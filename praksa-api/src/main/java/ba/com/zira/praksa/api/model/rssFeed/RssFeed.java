package ba.com.zira.praksa.api.model.rssFeed;

import java.io.Serializable;
import java.util.List;

import ba.com.zira.praksa.api.model.externalReview.ExternalReview;
import lombok.Data;

@Data
public class RssFeed implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String url;

    private List<ExternalReview> externalReviews;
}
