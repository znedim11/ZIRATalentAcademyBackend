package ba.com.zira.praksa.api.model.externalReview;

import java.io.Serializable;
import java.time.LocalDateTime;

import ba.com.zira.praksa.api.model.rssFeed.RssFeed;
import lombok.Data;

@Data
public class ExternalReview implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private LocalDateTime created;

    private String createdBy;

    private String information;

    private String origin;

    private RssFeed rss_feed;
}
