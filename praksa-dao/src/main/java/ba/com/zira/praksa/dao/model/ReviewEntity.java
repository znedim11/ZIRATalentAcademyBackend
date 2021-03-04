package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the hut_review database table.
 * 
 */
@Entity
@Table(name="hut_review")
@NamedQuery(name="ReviewEntity.findAll", query="SELECT r FROM ReviewEntity r")
public class ReviewEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="HUT_REVIEW_ID_GENERATOR", sequenceName="HUT_REVIEW_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HUT_REVIEW_ID_GENERATOR")
	private double id;

	private Timestamp created;

	@Column(name="created_by")
	private String createdBy;

	private Timestamp modified;

	@Column(name="modified_by")
	private String modifiedBy;

	private String text;

	private String title;

	//bi-directional many-to-one association to ReviewFormulaEntity
	@ManyToOne
	@JoinColumn(name="formula_id")
	private ReviewFormulaEntity hurReviewFormula;

	//bi-directional many-to-one association to GameEntity
	@ManyToOne
	@JoinColumn(name="game_id")
	private GameEntity hutGame;

	//bi-directional many-to-one association to ReviewGradeEntity
	@OneToMany(mappedBy="hutReview")
	private List<ReviewGradeEntity> hutReviewGrades;

	public ReviewEntity() {
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

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ReviewFormulaEntity getHurReviewFormula() {
		return this.hurReviewFormula;
	}

	public void setHurReviewFormula(ReviewFormulaEntity hurReviewFormula) {
		this.hurReviewFormula = hurReviewFormula;
	}

	public GameEntity getHutGame() {
		return this.hutGame;
	}

	public void setHutGame(GameEntity hutGame) {
		this.hutGame = hutGame;
	}

	public List<ReviewGradeEntity> getHutReviewGrades() {
		return this.hutReviewGrades;
	}

	public void setHutReviewGrades(List<ReviewGradeEntity> hutReviewGrades) {
		this.hutReviewGrades = hutReviewGrades;
	}

	public ReviewGradeEntity addHutReviewGrade(ReviewGradeEntity hutReviewGrade) {
		getHutReviewGrades().add(hutReviewGrade);
		hutReviewGrade.setHutReview(this);

		return hutReviewGrade;
	}

	public ReviewGradeEntity removeHutReviewGrade(ReviewGradeEntity hutReviewGrade) {
		getHutReviewGrades().remove(hutReviewGrade);
		hutReviewGrade.setHutReview(null);

		return hutReviewGrade;
	}

}