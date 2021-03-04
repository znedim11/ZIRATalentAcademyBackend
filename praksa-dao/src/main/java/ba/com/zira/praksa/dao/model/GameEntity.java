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
 * The persistent class for the hut_game database table.
 *
 */
@Entity
@Table(name = "hut_game")
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name = "GameEntity.findAll", query = "SELECT g FROM GameEntity g")
public class GameEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HUT_GAME_ID_GENERATOR", sequenceName = "HUT_GAME_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HUT_GAME_ID_GENERATOR")
    private double id;

    private String abbriviation;

    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    private String dlc;

    @Column(name = "full_name")
    private String fullName;

    private String genre;

    private String information;

    private LocalDateTime modified;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "outline_text")
    private String outlineText;

    // bi-directional many-to-one association to ExternalReviewEntity
    @OneToMany(mappedBy = "hutGame")
    private List<ExternalReviewEntity> hutExternalReviews;

    // bi-directional many-to-one association to FranchiseEntity
    @ManyToOne
    @JoinColumn(name = "franchise_id")
    private FranchiseEntity hutFranchise;

    // bi-directional many-to-one association to GameEntity
    @ManyToOne
    @JoinColumn(name = "dlc_game_id")
    private GameEntity hutGame;

    // bi-directional many-to-one association to GameEntity
    @OneToMany(mappedBy = "hutGame")
    private List<GameEntity> hutGames;

    // bi-directional many-to-one association to GameFeatureEntity
    @OneToMany(mappedBy = "hutGame")
    private List<GameFeatureEntity> hutGameFeatures;

    // bi-directional many-to-one association to LinkMapEntity
    @OneToMany(mappedBy = "hutGame")
    private List<LinkMapEntity> hutLinkMaps;

    // bi-directional many-to-one association to ReleaseEntity
    @OneToMany(mappedBy = "hutGame")
    private List<ReleaseEntity> hutReleases;

    // bi-directional many-to-one association to ReviewEntity
    @OneToMany(mappedBy = "hutGame")
    private List<ReviewEntity> hutReviews;

}