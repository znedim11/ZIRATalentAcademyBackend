package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the hut_feature database table.
 * 
 */
@Entity
@Table(name="hut_feature")
@NamedQuery(name="FeatureEntity.findAll", query="SELECT f FROM FeatureEntity f")
public class FeatureEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="HUT_FEATURE_ID_GENERATOR", sequenceName="HUT_FEATURE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HUT_FEATURE_ID_GENERATOR")
	private double id;

	private Timestamp created;

	@Column(name="created_by")
	private String createdBy;

	private String description;

	private Timestamp modified;

	@Column(name="modified_by")
	private String modifiedBy;

	private String name;

	private String type;

	//bi-directional many-to-one association to GameFeatureEntity
	@OneToMany(mappedBy="hutFeature")
	private List<GameFeatureEntity> hutGameFeatures;

	public FeatureEntity() {
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public List<GameFeatureEntity> getHutGameFeatures() {
		return this.hutGameFeatures;
	}

	public void setHutGameFeatures(List<GameFeatureEntity> hutGameFeatures) {
		this.hutGameFeatures = hutGameFeatures;
	}

	public GameFeatureEntity addHutGameFeature(GameFeatureEntity hutGameFeature) {
		getHutGameFeatures().add(hutGameFeature);
		hutGameFeature.setHutFeature(this);

		return hutGameFeature;
	}

	public GameFeatureEntity removeHutGameFeature(GameFeatureEntity hutGameFeature) {
		getHutGameFeatures().remove(hutGameFeature);
		hutGameFeature.setHutFeature(null);

		return hutGameFeature;
	}

}