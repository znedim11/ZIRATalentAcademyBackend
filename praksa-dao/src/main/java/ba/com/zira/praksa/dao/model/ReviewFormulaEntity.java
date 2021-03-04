package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the hur_review_formula database table.
 * 
 */
@Entity
@Table(name="hur_review_formula")
@NamedQuery(name="ReviewFormulaEntity.findAll", query="SELECT r FROM ReviewFormulaEntity r")
public class ReviewFormulaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="HUR_REVIEW_FORMULA_ID_GENERATOR", sequenceName="HUR_REVIEW_FORMULA_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HUR_REVIEW_FORMULA_ID_GENERATOR")
	private double id;

	private Timestamp created;

	@Column(name="created_by")
	private String createdBy;

	private String formula;

	private Timestamp modified;

	@Column(name="modified_by")
	private String modifiedBy;

	//bi-directional many-to-one association to ReviewEntity
	@OneToMany(mappedBy="hurReviewFormula")
	private List<ReviewEntity> hutReviews;

	public ReviewFormulaEntity() {
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

	public String getFormula() {
		return this.formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
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

	public List<ReviewEntity> getHutReviews() {
		return this.hutReviews;
	}

	public void setHutReviews(List<ReviewEntity> hutReviews) {
		this.hutReviews = hutReviews;
	}

	public ReviewEntity addHutReview(ReviewEntity hutReview) {
		getHutReviews().add(hutReview);
		hutReview.setHurReviewFormula(this);

		return hutReview;
	}

	public ReviewEntity removeHutReview(ReviewEntity hutReview) {
		getHutReviews().remove(hutReview);
		hutReview.setHurReviewFormula(null);

		return hutReview;
	}

}