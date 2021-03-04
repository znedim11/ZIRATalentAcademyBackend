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
 * The persistent class for the hut_media database table.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hut_media")
@NamedQuery(name = "MediaEntity.findAll", query = "SELECT m FROM MediaEntity m")
public class MediaEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "HUT_MEDIA_ID_GENERATOR", sequenceName = "HUT_MEDIA_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HUT_MEDIA_ID_GENERATOR")
    private Long id;

    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    private LocalDateTime modified;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "object_id")
    private double objectId;

    @Column(name = "object_type")
    private String objectType;

    // bi-directional many-to-one association to MediaStoreEntity
    @OneToMany(mappedBy = "media")
    private List<MediaStoreEntity> mediaStores;

}