package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the hut_review_grade database table.
 * 
 */
@Entity
@Table(name="hut_review_grade")
@NamedQuery(name="ReviewGradeEntity.findAll", query="SELECT r FROM ReviewGradeEntity r")
public class ReviewGradeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="HUT_REVIEW_GRADE_UUID_GENERATOR", sequenceName="HUT_REVIEW_GRADE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HUT_REVIEW_GRADE_UUID_GENERATOR")
	private String uuid;

	@Column(name="formula_id")
	private double formulaId;

	private double grade;

	private String type;

	//bi-directional many-to-one association to ReviewEntity
	@ManyToOne
	@JoinColumn(name="review_id")
	private ReviewEntity hutReview;

	public ReviewGradeEntity() {
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public double getFormulaId() {
		return this.formulaId;
	}

	public void setFormulaId(double formulaId) {
		this.formulaId = formulaId;
	}

	public double getGrade() {
		return this.grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ReviewEntity getHutReview() {
		return this.hutReview;
	}

	public void setHutReview(ReviewEntity hutReview) {
		this.hutReview = hutReview;
	}

}