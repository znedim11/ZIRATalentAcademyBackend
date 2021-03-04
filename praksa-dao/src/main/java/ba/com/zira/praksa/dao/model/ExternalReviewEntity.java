package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the hut_external_review database table.
 * 
 */
@Entity
@Table(name="hut_external_review")
@NamedQuery(name="ExternalReviewEntity.findAll", query="SELECT e FROM ExternalReviewEntity e")
public class ExternalReviewEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="HUT_EXTERNAL_REVIEW_ID_GENERATOR", sequenceName="HUT_EXTERNAL_REVIEW_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HUT_EXTERNAL_REVIEW_ID_GENERATOR")
	private double id;

	private Timestamp created;

	@Column(name="created_by")
	private String createdBy;

	private String information;

	private String origin;

	@Column(name="rss_feed_id")
	private double rssFeedId;

	//bi-directional one-to-one association to RssFeedEntity
	@OneToOne
	@JoinColumn(name="id")
	private RssFeedEntity hurRssFeed;

	//bi-directional many-to-one association to GameEntity
	@ManyToOne
	@JoinColumn(name="game_id")
	private GameEntity hutGame;

	public ExternalReviewEntity() {
	}

	public double getId() {
		return this.id;
	}

	public void setId(double id) {
		this.id = id;
	}

	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getInformation() {
		return this.information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public String getOrigin() {
		return this.origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public double getRssFeedId() {
		return this.rssFeedId;
	}

	public void setRssFeedId(double rssFeedId) {
		this.rssFeedId = rssFeedId;
	}

	public RssFeedEntity getHurRssFeed() {
		return this.hurRssFeed;
	}

	public void setHurRssFeed(RssFeedEntity hurRssFeed) {
		this.hurRssFeed = hurRssFeed;
	}

	public GameEntity getHutGame() {
		return this.hutGame;
	}

	public void setHutGame(GameEntity hutGame) {
		this.hutGame = hutGame;
	}

}