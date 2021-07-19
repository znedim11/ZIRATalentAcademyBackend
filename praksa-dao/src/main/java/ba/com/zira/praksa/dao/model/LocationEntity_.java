package ba.com.zira.praksa.dao.model;

import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LocationEntity.class)
public abstract class LocationEntity_ {
    public static volatile SingularAttribute<LocationEntity, Long> id;
    public static volatile SingularAttribute<LocationEntity, String> aliases;
    public static volatile SingularAttribute<LocationEntity, LocalDateTime> created;
    public static volatile SingularAttribute<LocationEntity, String> createdBy;
    public static volatile SingularAttribute<LocationEntity, String> information;
    public static volatile SingularAttribute<LocationEntity, LocalDateTime> modified;
    public static volatile SingularAttribute<LocationEntity, String> modifiedBy;
    public static volatile SingularAttribute<LocationEntity, String> name;
    public static volatile ListAttribute<LocationEntity, LinkMapEntity> linkMaps;
}
