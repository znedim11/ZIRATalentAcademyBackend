package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the hut_review_grade database table.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hur_grade")
@NamedQuery(name = "GradeEntity.findAll", query = "SELECT r FROM GradeEntity r")
public class GradeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HUR_GRADE_ID_GENERATOR", sequenceName = "HUR_GRADE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HUR_GRADE_ID_GENERATOR")
    private Long id;

    @Column(name = "formula_id")
    private Long formulaId;

    private String type;

    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;
}