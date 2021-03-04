package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the hut_concept database table.
 * 
 */
@Entity
@Table(name="hut_concept")
@NamedQuery(name="ConceptEntity.findAll", query="SELECT c FROM ConceptEntity c")
public class ConceptEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="HUT_CONCEPT_ID_GENERATOR", sequenceName="HUT_CONCEPT_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HUT_CONCEPT_ID_GENERATOR")
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
	@OneToMany(mappedBy="hutConcept")
	private List<LinkMapEntity> hutLinkMaps;

	public ConceptEntity() {
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
		hutLinkMap.setHutConcept(this);

		return hutLinkMap;
	}

	public LinkMapEntity removeHutLinkMap(LinkMapEntity hutLinkMap) {
		getHutLinkMaps().remove(hutLinkMap);
		hutLinkMap.setHutConcept(null);

		return hutLinkMap;
	}

}