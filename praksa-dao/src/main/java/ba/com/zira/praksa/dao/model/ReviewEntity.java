package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the hut_review database table.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hut_review")
@NamedQuery(name = "ReviewEntity.findAll", query = "SELECT r FROM ReviewEntity r")
public class ReviewEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HUT_REVIEW_ID_GENERATOR", sequenceName = "HUT_REVIEW_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HUT_REVIEW_ID_GENERATOR")
    private Long id;

    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    private LocalDateTime modified;

    @Column(name = "modified_by")
    private String modifiedBy;

    private String text;

    private String title;

    // bi-directional many-to-one association to ReviewFormulaEntity
    @ManyToOne
    @JoinColumn(name = "formula_id")
    private ReviewFormulaEntity reviewFormula;

    // bi-directional many-to-one association to GameEntity
    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameEntity game;

    // bi-directional many-to-one association to ReviewGradeEntity
    @OneToMany(mappedBy = "review")
    private List<ReviewGradeEntity> reviewGrades;

}