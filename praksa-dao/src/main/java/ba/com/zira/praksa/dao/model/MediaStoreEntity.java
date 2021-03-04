package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the hut_media_store database table.
 * 
 */
@Entity
@Table(name="hut_media_store")
@NamedQuery(name="MediaStoreEntity.findAll", query="SELECT m FROM MediaStoreEntity m")
public class MediaStoreEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="HUT_MEDIA_STORE_UUID_GENERATOR", sequenceName="HUT_MEDIA_STORE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HUT_MEDIA_STORE_UUID_GENERATOR")
	private String uuid;

	private Timestamp created;

	@Column(name="created_by")
	private String createdBy;

	private byte[] data;

	private Timestamp modified;

	@Column(name="modified_by")
	private String modifiedBy;

	private String name;

	private String type;

	//bi-directional many-to-one association to MediaEntity
	@ManyToOne
	@JoinColumn(name="media_id")
	private MediaEntity hutMedia;

	public MediaStoreEntity() {
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public byte[] getData() {
		return this.data;
	}

	public void setData(byte[] data) {
		this.data = data;
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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MediaEntity getHutMedia() {
		return this.hutMedia;
	}

	public void setHutMedia(MediaEntity hutMedia) {
		this.hutMedia = hutMedia;
	}

}