package ba.com.zira.praksa.dao.model;

import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ObjectEntity.class)
public abstract class ObjectEntity_ {
    public static volatile SingularAttribute<ObjectEntity, Long> id;
    public static volatile SingularAttribute<ObjectEntity, String> aliases;
    public static volatile SingularAttribute<ObjectEntity, LocalDateTime> created;
    public static volatile SingularAttribute<ObjectEntity, String> createdBy;
    public static volatile SingularAttribute<ObjectEntity, String> information;
    public static volatile SingularAttribute<ObjectEntity, LocalDateTime> modified;
    public static volatile SingularAttribute<ObjectEntity, String> modifiedBy;
    public static volatile SingularAttribute<ObjectEntity, String> name;
    public static volatile ListAttribute<ObjectEntity, LinkMapEntity> linkMaps;
}
