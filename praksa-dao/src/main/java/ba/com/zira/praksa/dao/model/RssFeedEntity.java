package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the hur_rss_feed database table.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hur_rss_feed")
@NamedQuery(name = "RssFeedEntity.findAll", query = "SELECT r FROM RssFeedEntity r")
public class RssFeedEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HUR_RSS_FEED_ID_GENERATOR", sequenceName = "HUR_RSS_FEED_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HUR_RSS_FEED_ID_GENERATOR")
    private Long id;

    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    private LocalDateTime modified;

    @Column(name = "modified_by")
    private String modifiedBy;

    private String name;

    private String url;

    // bi-directional one-to-one association to ExternalReviewEntity
    @OneToMany(mappedBy = "rssFeed")
    private List<ExternalReviewEntity> externalReviews;

}