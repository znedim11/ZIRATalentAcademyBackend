package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the hut_release database table.
 *
 */
@Entity
@Table(name = "hut_release")
@NamedQuery(name = "ReleaseEntity.findAll", query = "SELECT r FROM ReleaseEntity r")
public class ReleaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HUT_RELEASE_UUID_GENERATOR", sequenceName = "HUT_RELEASE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HUT_RELEASE_UUID_GENERATOR")
    private String uuid;

    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    private LocalDateTime modified;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    private String type;

    // bi-directional many-to-one association to CompanyEntity
    @ManyToOne
    @JoinColumn(name = "developer_id")
    private CompanyEntity developer;

    // bi-directional many-to-one association to CompanyEntity
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private CompanyEntity publisher;

    // bi-directional many-to-one association to GameEntity
    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameEntity game;

    // bi-directional many-to-one association to PlatformEntity
    @ManyToOne
    @JoinColumn(name = "platform_id")
    private PlatformEntity platform;

    // bi-directional many-to-one association to RegionEntity
    @ManyToOne
    @JoinColumn(name = "region_id")
    private RegionEntity region;

}