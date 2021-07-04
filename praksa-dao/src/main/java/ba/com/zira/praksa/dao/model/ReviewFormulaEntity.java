package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the hur_review_formula database table.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hur_review_formula")
@NamedQuery(name = "ReviewFormulaEntity.findAll", query = "SELECT r FROM ReviewFormulaEntity r")
public class ReviewFormulaEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HUR_REVIEW_FORMULA_ID_GENERATOR", sequenceName = "HUR_REVIEW_FORMULA_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HUR_REVIEW_FORMULA_ID_GENERATOR")
    private Long id;

    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    private String formula;
    private String name;

    private LocalDateTime modified;

    @Column(name = "modified_by")
    private String modifiedBy;

    // bi-directional many-to-one association to ReviewEntity
    @OneToMany(mappedBy = "reviewFormula")
    private List<ReviewEntity> reviews;

}