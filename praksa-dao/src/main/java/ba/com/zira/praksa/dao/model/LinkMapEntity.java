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
 * The persistent class for the hut_link_map database table.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hut_link_map")
@NamedQuery(name = "LinkMapEntity.findAll", query = "SELECT l FROM LinkMapEntity l")
public class LinkMapEntity implements Serializable {
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

    @Column(name = "person_id")
    private Long personId;

    // bi-directional many-to-one association to CharacterEntity
    @ManyToOne
    @JoinColumn(name = "character_id")
    private CharacterEntity character;

    // bi-directional many-to-one association to ConceptEntity
    @ManyToOne
    @JoinColumn(name = "concept_id")
    private ConceptEntity concept;

    // bi-directional many-to-one association to GameEntity
    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameEntity game;

    // bi-directional many-to-one association to LocationEntity
    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    // bi-directional many-to-one association to ObjectEntity
    @ManyToOne
    @JoinColumn(name = "object_id")
    private ObjectEntity object;

    // bi-directional many-to-one association to PersonEntity
    @ManyToOne
    @JoinColumn(name = "character_id")
    private PersonEntity person;

}