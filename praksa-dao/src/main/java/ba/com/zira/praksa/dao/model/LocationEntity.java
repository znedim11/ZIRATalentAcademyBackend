package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the hut_location database table.
 * 
 */
@Entity
@Table(name="hut_location")
@NamedQuery(name="LocationEntity.findAll", query="SELECT l FROM LocationEntity l")
public class LocationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="HUT_LOCATION_ID_GENERATOR", sequenceName="HUT_LOCATION_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HUT_LOCATION_ID_GENERATOR")
	private double id;

	private String aliases;

	private Timestamp created;

	@Column(name="created_by")
	private String createdBy;

	private String information;

	private Timestamp modified;

	@Column(name="modified_by")
	private String modifiedBy;

	private String name;

	//bi-directional many-to-one association to LinkMapEntity
	@OneToMany(mappedBy="hutLocation")
	private List<LinkMapEntity> hutLinkMaps;

	public LocationEntity() {
	}

	public double getId() {
		return this.id;
	}

	public void setId(double id) {
		this.id = id;
	}

	public String getAliases() {
		return this.aliases;
	}

	public void setAliases(String aliases) {
		this.aliases = aliases;
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

	public List<LinkMapEntity> getHutLinkMaps() {
		return this.hutLinkMaps;
	}

	public void setHutLinkMaps(List<LinkMapEntity> hutLinkMaps) {
		this.hutLinkMaps = hutLinkMaps;
	}

	public LinkMapEntity addHutLinkMap(LinkMapEntity hutLinkMap) {
		getHutLinkMaps().add(hutLinkMap);
		hutLinkMap.setHutLocation(this);

		return hutLinkMap;
	}

	public LinkMapEntity removeHutLinkMap(LinkMapEntity hutLinkMap) {
		getHutLinkMaps().remove(hutLinkMap);
		hutLinkMap.setHutLocation(null);

		return hutLinkMap;
	}

}