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
 * The persistent class for the hut_person database table.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hut_person")
@NamedQuery(name = "PersonEntity.findAll", query = "SELECT p FROM PersonEntity p")
public class PersonEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HUT_PERSON_ID_GENERATOR", sequenceName = "HUT_PERSON_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HUT_PERSON_ID_GENERATOR")
    private Long id;

    private String aliases;

    private String city;

    private String country;

    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    private LocalDateTime dob;

    private LocalDateTime dod;

    private String email;

    @Column(name = "first_name")
    private String firstName;

    private String gender;

    private String information;

    @Column(name = "last_name")
    private String lastName;

    private LocalDateTime modified;

    @Column(name = "modified_by")
    private String modifiedBy;

    private String twitter;

    private String website;

    // bi-directional many-to-one association to LinkMapEntity
    @OneToMany(mappedBy = "person")
    private List<LinkMapEntity> linkMaps;

}