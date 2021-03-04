package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the hur_rss_feed database table.
 * 
 */
@Entity
@Table(name="hur_rss_feed")
@NamedQuery(name="RssFeedEntity.findAll", query="SELECT r FROM RssFeedEntity r")
public class RssFeedEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="HUR_RSS_FEED_ID_GENERATOR", sequenceName="HUR_RSS_FEED_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HUR_RSS_FEED_ID_GENERATOR")
	private double id;

	private Timestamp created;

	@Column(name="created_by")
	private String createdBy;

	private Timestamp modified;

	@Column(name="modified_by")
	private String modifiedBy;

	private String name;

	private String url;

	//bi-directional one-to-one association to ExternalReviewEntity
	@OneToOne(mappedBy="hurRssFeed")
	private ExternalReviewEntity hutExternalReview;

	public RssFeedEntity() {
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

	public Timestamp getModified() {
		return this.modified;
	}

	public void setModified(Timestamp modified) {
		this.modified = modified;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ExternalReviewEntity getHutExternalReview() {
		return this.hutExternalReview;
	}

	public void setHutExternalReview(ExternalReviewEntity hutExternalReview) {
		this.hutExternalReview = hutExternalReview;
	}

}