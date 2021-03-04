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
 * The persistent class for the hut_character database table.
 *
 */
@Entity
@Table(name = "hut_character")
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name = "CharacterEntity.findAll", query = "SELECT c FROM CharacterEntity c")
public class CharacterEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HUT_CHARACTER_ID_GENERATOR", sequenceName = "HUT_CHARACTER_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HUT_CHARACTER_ID_GENERATOR")
    private double id;

    private String aliases;

    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    private LocalDateTime dob;

    private LocalDateTime dod;

    private String gender;

    private String information;

    private LocalDateTime modified;

    @Column(name = "modified_by")
    private String modifiedBy;

    private String name;

    @Column(name = "real_name")
    private String realName;

    // bi-directional many-to-one association to LinkMapEntity
    @OneToMany(mappedBy = "hutCharacter")
    private List<LinkMapEntity> hutLinkMaps;

}