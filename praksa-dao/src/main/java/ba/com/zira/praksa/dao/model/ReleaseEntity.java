package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the hut_release database table.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hut_release")
@NamedQuery(name = "ReleaseEntity.findAll", query = "SELECT r FROM ReleaseEntity r")
public class ReleaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
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

    @Override
    public String toString() {
        return "ReleaseEntity [uuid=" + uuid + ", created=" + created + ", createdBy=" + createdBy + ", modified=" + modified
                + ", modifiedBy=" + modifiedBy + ", releaseDate=" + releaseDate + ", type=" + type + ", game=" + game + ", platform="
                + +platform.getId() + ". " + platform.getFullName() + ", region=" + region.getId() + "]";
    }

}