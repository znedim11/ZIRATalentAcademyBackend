package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the hut_external_review database table.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hut_external_review")
@NamedQuery(name = "ExternalReviewEntity.findAll", query = "SELECT e FROM ExternalReviewEntity e")
public class ExternalReviewEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HUT_EXTERNAL_REVIEW_ID_GENERATOR", sequenceName = "HUT_EXTERNAL_REVIEW_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HUT_EXTERNAL_REVIEW_ID_GENERATOR")
    private Long id;

    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    private String information;

    private String origin;

    @Column(name = "game_name")
    private String gameName;

    // bi-directional one-to-one association to RssFeedEntity
    @ManyToOne
    @JoinColumn(name = "rss_feed_id")
    private RssFeedEntity rssFeed;

    // bi-directional many-to-one association to GameEntity
    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameEntity game;

}