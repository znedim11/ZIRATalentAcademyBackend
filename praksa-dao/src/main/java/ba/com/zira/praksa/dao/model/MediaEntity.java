package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the hut_media database table.
 * 
 */
@Entity
@Table(name="hut_media")
@NamedQuery(name="MediaEntity.findAll", query="SELECT m FROM MediaEntity m")
public class MediaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="HUT_MEDIA_ID_GENERATOR", sequenceName="HUT_MEDIA_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HUT_MEDIA_ID_GENERATOR")
	private double id;

	private Timestamp created;

	@Column(name="created_by")
	private String createdBy;

	private Timestamp modified;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="object_id")
	private double objectId;

	@Column(name="object_type")
	private String objectType;

	//bi-directional many-to-one association to MediaStoreEntity
	@OneToMany(mappedBy="hutMedia")
	private List<MediaStoreEntity> hutMediaStores;

	public MediaEntity() {
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

	public double getObjectId() {
		return this.objectId;
	}

	public void setObjectId(double objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return this.objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public List<MediaStoreEntity> getHutMediaStores() {
		return this.hutMediaStores;
	}

	public void setHutMediaStores(List<MediaStoreEntity> hutMediaStores) {
		this.hutMediaStores = hutMediaStores;
	}

	public MediaStoreEntity addHutMediaStore(MediaStoreEntity hutMediaStore) {
		getHutMediaStores().add(hutMediaStore);
		hutMediaStore.setHutMedia(this);

		return hutMediaStore;
	}

	public MediaStoreEntity removeHutMediaStore(MediaStoreEntity hutMediaStore) {
		getHutMediaStores().remove(hutMediaStore);
		hutMediaStore.setHutMedia(null);

		return hutMediaStore;
	}

}