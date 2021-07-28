package ba.com.zira.praksa.dao.model;

import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RegionEntity.class)
public abstract class RegionEntity_ {
    public static volatile SingularAttribute<RegionEntity, Long> id;
    public static volatile SingularAttribute<RegionEntity, LocalDateTime> created;
    public static volatile SingularAttribute<RegionEntity, String> createdBy;
    public static volatile SingularAttribute<RegionEntity, RegionEntity> description;
    public static volatile SingularAttribute<RegionEntity, LocalDateTime> modified;
    public static volatile SingularAttribute<RegionEntity, String> modifiedBy;
    public static volatile SingularAttribute<RegionEntity, String> name;
    public static volatile ListAttribute<RegionEntity, ReleaseEntity> releases;

}
