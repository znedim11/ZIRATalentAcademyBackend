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
 * The persistent class for the hut_media_store database table.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hut_media_store")
@NamedQuery(name = "MediaStoreEntity.findAll", query = "SELECT m FROM MediaStoreEntity m")
public class MediaStoreEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "hibernate-uuid")
    @Column(name = "UUID", unique = true)
    private String uuid;

    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    private byte[] data;

    private LocalDateTime modified;

    @Column(name = "modified_by")
    private String modifiedBy;

    private String name;

    private String type;

    // bi-directional many-to-one association to MediaEntity
    @ManyToOne
    @JoinColumn(name = "media_id")
    private MediaEntity media;

}