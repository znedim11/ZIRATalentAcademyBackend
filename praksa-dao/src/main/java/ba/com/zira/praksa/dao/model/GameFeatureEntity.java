package ba.com.zira.praksa.dao.model;

import java.io.Serializable;
import java.time.LocalDateTime;

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
 * The persistent class for the hut_game_feature database table.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hut_game_feature")
@NamedQuery(name = "GameFeatureEntity.findAll", query = "SELECT g FROM GameFeatureEntity g")
public class GameFeatureEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "hibernate-uuid")
    @Column(name = "UUID", unique = true)
    private String uuid;

    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    private LocalDateTime modified;

    @Column(name = "modified_by")
    private String modifiedBy;

    // bi-directional many-to-one association to FeatureEntity
    @ManyToOne
    @JoinColumn(name = "feature_id")
    private FeatureEntity feature;

    // bi-directional many-to-one association to GameEntity
    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameEntity game;

}