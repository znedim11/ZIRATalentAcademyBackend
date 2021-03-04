package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the hut_link_map database table.
 * 
 */
@Entity
@Table(name="hut_link_map")
@NamedQuery(name="LinkMapEntity.findAll", query="SELECT l FROM LinkMapEntity l")
public class LinkMapEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="HUT_LINK_MAP_UUID_GENERATOR", sequenceName="HUT_LINK_MAP_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HUT_LINK_MAP_UUID_GENERATOR")
	private String uuid;

	private Timestamp created;

	@Column(name="created_by")
	private String createdBy;

	private Timestamp modified;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="person_id")
	private double personId;

	//bi-directional many-to-one association to CharacterEntity
	@ManyToOne
	@JoinColumn(name="character_id")
	private CharacterEntity hutCharacter;

	//bi-directional many-to-one association to ConceptEntity
	@ManyToOne
	@JoinColumn(name="concept_id")
	private ConceptEntity hutConcept;

	//bi-directional many-to-one association to GameEntity
	@ManyToOne
	@JoinColumn(name="game_id")
	private GameEntity hutGame;

	//bi-directional many-to-one association to LocationEntity
	@ManyToOne
	@JoinColumn(name="location_id")
	private LocationEntity hutLocation;

	//bi-directional many-to-one association to ObjectEntity
	@ManyToOne
	@JoinColumn(name="object_id")
	private ObjectEntity hutObject;

	//bi-directional many-to-one association to PersonEntity
	@ManyToOne
	@JoinColumn(name="character_id")
	private PersonEntity hutPerson;

	public LinkMapEntity() {
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

	public double getPersonId() {
		return this.personId;
	}

	public void setPersonId(double personId) {
		this.personId = personId;
	}

	public CharacterEntity getHutCharacter() {
		return this.hutCharacter;
	}

	public void setHutCharacter(CharacterEntity hutCharacter) {
		this.hutCharacter = hutCharacter;
	}

	public ConceptEntity getHutConcept() {
		return this.hutConcept;
	}

	public void setHutConcept(ConceptEntity hutConcept) {
		this.hutConcept = hutConcept;
	}

	public GameEntity getHutGame() {
		return this.hutGame;
	}

	public void setHutGame(GameEntity hutGame) {
		this.hutGame = hutGame;
	}

	public LocationEntity getHutLocation() {
		return this.hutLocation;
	}

	public void setHutLocation(LocationEntity hutLocation) {
		this.hutLocation = hutLocation;
	}

	public ObjectEntity getHutObject() {
		return this.hutObject;
	}

	public void setHutObject(ObjectEntity hutObject) {
		this.hutObject = hutObject;
	}

	public PersonEntity getHutPerson() {
		return this.hutPerson;
	}

	public void setHutPerson(PersonEntity hutPerson) {
		this.hutPerson = hutPerson;
	}

}