package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the hut_franchise database table.
 * 
 */
@Entity
@Table(name="hut_franchise")
@NamedQuery(name="FranchiseEntity.findAll", query="SELECT f FROM FranchiseEntity f")
public class FranchiseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="HUT_FRANCHISE_ID_GENERATOR", sequenceName="HUT_FRANCHISE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HUT_FRANCHISE_ID_GENERATOR")
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

	@Column(name="outline_text")
	private String outlineText;

	//bi-directional many-to-one association to GameEntity
	@OneToMany(mappedBy="hutFranchise")
	private List<GameEntity> hutGames;

	public FranchiseEntity() {
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

	public String getOutlineText() {
		return this.outlineText;
	}

	public void setOutlineText(String outlineText) {
		this.outlineText = outlineText;
	}

	public List<GameEntity> getHutGames() {
		return this.hutGames;
	}

	public void setHutGames(List<GameEntity> hutGames) {
		this.hutGames = hutGames;
	}

	public GameEntity addHutGame(GameEntity hutGame) {
		getHutGames().add(hutGame);
		hutGame.setHutFranchise(this);

		return hutGame;
	}

	public GameEntity removeHutGame(GameEntity hutGame) {
		getHutGames().remove(hutGame);
		hutGame.setHutFranchise(null);

		return hutGame;
	}

}