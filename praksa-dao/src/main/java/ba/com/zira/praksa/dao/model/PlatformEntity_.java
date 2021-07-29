package ba.com.zira.praksa.dao.model;

import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PlatformEntity.class)
public abstract class PlatformEntity_ {
    public static volatile SingularAttribute<PlatformEntity, Long> id;
    public static volatile SingularAttribute<PlatformEntity, String> abbriviation;
    public static volatile SingularAttribute<PlatformEntity, String> code;
    public static volatile SingularAttribute<PlatformEntity, LocalDateTime> created;
    public static volatile SingularAttribute<PlatformEntity, String> createdBy;
    public static volatile SingularAttribute<PlatformEntity, String> fullName;
    public static volatile SingularAttribute<PlatformEntity, String> aliases;
    public static volatile SingularAttribute<PlatformEntity, String> information;
    public static volatile SingularAttribute<PlatformEntity, LocalDateTime> modified;
    public static volatile SingularAttribute<PlatformEntity, String> modifiedBy;
    public static volatile SingularAttribute<PlatformEntity, String> outlineText;
    public static volatile ListAttribute<PlatformEntity, ReleaseEntity> releases;
}
