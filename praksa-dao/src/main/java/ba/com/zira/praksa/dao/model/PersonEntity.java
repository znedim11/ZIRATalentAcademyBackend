package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the hut_person database table.
 * 
 */
@Entity
@Table(name="hut_person")
@NamedQuery(name="PersonEntity.findAll", query="SELECT p FROM PersonEntity p")
public class PersonEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="HUT_PERSON_ID_GENERATOR", sequenceName="HUT_PERSON_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HUT_PERSON_ID_GENERATOR")
	private double id;

	private String aliases;

	private String city;

	private String country;

	private Timestamp created;

	@Column(name="created_by")
	private String createdBy;

	private Timestamp dob;

	private Timestamp dod;

	private String email;

	@Column(name="first_name")
	private String firstName;

	private String gender;

	private String information;

	@Column(name="last_name")
	private String lastName;

	private Timestamp modified;

	@Column(name="modified_by")
	private String modifiedBy;

	private String twitter;

	private String website;

	//bi-directional many-to-one association to LinkMapEntity
	@OneToMany(mappedBy="hutPerson")
	private List<LinkMapEntity> hutLinkMaps;

	public PersonEntity() {
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

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public Timestamp getDob() {
		return this.dob;
	}

	public void setDob(Timestamp dob) {
		this.dob = dob;
	}

	public Timestamp getDod() {
		return this.dod;
	}

	public void setDod(Timestamp dod) {
		this.dod = dod;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getInformation() {
		return this.information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getTwitter() {
		return this.twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public List<LinkMapEntity> getHutLinkMaps() {
		return this.hutLinkMaps;
	}

	public void setHutLinkMaps(List<LinkMapEntity> hutLinkMaps) {
		this.hutLinkMaps = hutLinkMaps;
	}

	public LinkMapEntity addHutLinkMap(LinkMapEntity hutLinkMap) {
		getHutLinkMaps().add(hutLinkMap);
		hutLinkMap.setHutPerson(this);

		return hutLinkMap;
	}

	public LinkMapEntity removeHutLinkMap(LinkMapEntity hutLinkMap) {
		getHutLinkMaps().remove(hutLinkMap);
		hutLinkMap.setHutPerson(null);

		return hutLinkMap;
	}

}