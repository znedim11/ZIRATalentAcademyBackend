package ba.com.zira.praksa.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

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
@Table(name = "hut_review_grade")
@NamedQuery(name = "ReviewGradeEntity.findAll", query = "SELECT r FROM ReviewGradeEntity r")
public class ReviewGradeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "hibernate-uuid")
    @Column(name = "UUID", unique = true)
    private String uuid;

    @Column(name = "formula_id")
    private Long formulaId;

    private Long grade;

    private String type;

    // bi-directional many-to-one association to ReviewEntity
    @ManyToOne
    @JoinColumn(name = "review_id")
    private ReviewEntity review;

}