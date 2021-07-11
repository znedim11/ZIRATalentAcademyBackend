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
 * The persistent class for the hut_company database table.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hut_company")
@NamedQuery(name = "CompanyEntity.findAll", query = "SELECT c FROM CompanyEntity c")
public class CompanyEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HUT_COMPANY_ID_GENERATOR", sequenceName = "HUT_COMPANY_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HUT_COMPANY_ID_GENERATOR")
    private Long id;

    private String address;

    private String city;

    private String country;

    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    private String email;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    private String information;

    private LocalDateTime modified;

    @Column(name = "modified_by")
    private String modifiedBy;

    private String name;

    @Column(name = "outline_text")
    private String outlineText;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "tel_number")
    private String telNumber;

    private String website;

    // bi-directional many-to-one association to ReleaseEntity
    @OneToMany(mappedBy = "publisher")
    private List<ReleaseEntity> publisherReleases;

    // bi-directional many-to-one association to ReleaseEntity
    @OneToMany(mappedBy = "developer")
    private List<ReleaseEntity> developerReleases;

    @Override
    public String toString() {
        return "CompanyEntity [id=" + id + ", address=" + address + ", city=" + city + ", country=" + country + ", created=" + created
                + ", createdBy=" + createdBy + ", email=" + email + ", endDate=" + endDate + ", information=" + information + ", modified="
                + modified + ", modifiedBy=" + modifiedBy + ", name=" + name + ", outlineText=" + outlineText + ", startDate=" + startDate
                + ", telNumber=" + telNumber + ", website=" + website + ", publisherReleases=" + publisherReleases + ", developerReleases="
                + developerReleases + "]";
    }

}