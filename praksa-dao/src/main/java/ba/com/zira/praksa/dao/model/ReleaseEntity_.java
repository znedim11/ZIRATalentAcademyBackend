package ba.com.zira.praksa.dao.model;

import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ReleaseEntity.class)
public abstract class ReleaseEntity_ {
    public static volatile SingularAttribute<ReleaseEntity, String> uuid;
    public static volatile SingularAttribute<ReleaseEntity, LocalDateTime> created;
    public static volatile SingularAttribute<ReleaseEntity, String> createdBy;
    public static volatile SingularAttribute<ReleaseEntity, LocalDateTime> modified;
    public static volatile SingularAttribute<ReleaseEntity, String> modifiedBy;
    public static volatile SingularAttribute<ReleaseEntity, LocalDateTime> releaseDate;
    public static volatile SingularAttribute<ReleaseEntity, String> type;
    public static volatile ListAttribute<ReleaseEntity, CompanyEntity> developer;
    public static volatile ListAttribute<ReleaseEntity, CompanyEntity> publisher;
    public static volatile ListAttribute<ReleaseEntity, GameEntity> game;
    public static volatile ListAttribute<ReleaseEntity, PlatformEntity> platform;
    public static volatile ListAttribute<ReleaseEntity, RegionEntity> region;
}
