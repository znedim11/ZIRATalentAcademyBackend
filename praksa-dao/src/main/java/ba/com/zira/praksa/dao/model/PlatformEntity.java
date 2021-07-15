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
 * The persistent class for the hut_platform database table.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hut_platform")
@NamedQuery(name = "PlatformEntity.findAll", query = "SELECT p FROM PlatformEntity p")
public class PlatformEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HUT_PLATFORM_ID_GENERATOR", sequenceName = "HUT_PLATFORM_SEQ", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HUT_PLATFORM_ID_GENERATOR")
    private Long id;

    private String abbriviation;

    private String code;

    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "full_name")
    private String fullName;

    private String information;

    private LocalDateTime modified;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "outline_text")
    private String outlineText;

    // bi-directional many-to-one association to ReleaseEntity
    @OneToMany(mappedBy = "platform")
    private List<ReleaseEntity> releases;

}