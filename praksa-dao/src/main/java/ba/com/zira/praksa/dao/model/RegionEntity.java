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
 * The persistent class for the hut_region database table.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hut_region")
@NamedQuery(name = "RegionEntity.findAll", query = "SELECT r FROM RegionEntity r")
public class RegionEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HUT_REGION_ID_GENERATOR", sequenceName = "HUT_REGION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HUT_REGION_ID_GENERATOR")
    private double id;

    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    private String description;

    private LocalDateTime modified;

    @Column(name = "modified_by")
    private String modifiedBy;

    private String name;

    // bi-directional many-to-one association to ReleaseEntity
    @OneToMany(mappedBy = "hutRegion")
    private List<ReleaseEntity> hutReleases;

}