package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the hut_game_feature database table.
 * 
 */
@Entity
@Table(name="hut_game_feature")
@NamedQuery(name="GameFeatureEntity.findAll", query="SELECT g FROM GameFeatureEntity g")
public class GameFeatureEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="HUT_GAME_FEATURE_UUID_GENERATOR", sequenceName="HUT_GAME_FEATURE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HUT_GAME_FEATURE_UUID_GENERATOR")
	private String uuid;

	private Timestamp created;

	@Column(name="created_by")
	private String createdBy;

	private Timestamp modified;

	@Column(name="modified_by")
	private String modifiedBy;

	//bi-directional many-to-one association to FeatureEntity
	@ManyToOne
	@JoinColumn(name="feature_id")
	private FeatureEntity hutFeature;

	//bi-directional many-to-one association to GameEntity
	@ManyToOne
	@JoinColumn(name="game_id")
	private GameEntity hutGame;

	public GameFeatureEntity() {
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

	public FeatureEntity getHutFeature() {
		return this.hutFeature;
	}

	public void setHutFeature(FeatureEntity hutFeature) {
		this.hutFeature = hutFeature;
	}

	public GameEntity getHutGame() {
		return this.hutGame;
	}

	public void setHutGame(GameEntity hutGame) {
		this.hutGame = hutGame;
	}

}