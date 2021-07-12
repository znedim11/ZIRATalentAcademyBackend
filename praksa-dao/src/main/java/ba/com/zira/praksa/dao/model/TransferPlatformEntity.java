package ba.com.zira.praksa.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
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
@Table(name = "hus_platform")
@NamedQuery(name = "TransferPlatformEntity.findAll", query = "SELECT p FROM TransferPlatformEntity p")
public class TransferPlatformEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HUS_PLATFORM_ID_GENERATOR", sequenceName = "HUS_PLATFORM_SEQ", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HUS_PLATFORM_ID_GENERATOR")
    private Long id;

    private String name;

    private String emulated;

    @Column(name = "release_date")
    private String releaseDate;

    private String developer;

    private String manufacturer;

    private String cpu;

    private String memory;

    private String graphics;

    private String sound;

    private String display;

    private String media;

    private String notes;

    private String category;

    @Column(name = "use_mame_files")
    private String useMameFiles;

    @Column(name = "max_controllers")
    private String maxControles;

    @Column(name = "platform_alternate_name")
    private String platformAlternateName;

    public TransferPlatformEntity(String name) {
        this.name = name;
    }
}