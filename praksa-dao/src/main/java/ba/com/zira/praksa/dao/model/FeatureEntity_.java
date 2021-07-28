package ba.com.zira.praksa.dao.model;

import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FeatureEntity.class)
public abstract class FeatureEntity_ {
    public static volatile SingularAttribute<FeatureEntity, Long> id;
    public static volatile SingularAttribute<FeatureEntity, LocalDateTime> created;
    public static volatile SingularAttribute<FeatureEntity, String> createdBy;
    public static volatile SingularAttribute<FeatureEntity, String> description;
    public static volatile SingularAttribute<FeatureEntity, LocalDateTime> modified;
    public static volatile SingularAttribute<FeatureEntity, String> modifiedBy;
    public static volatile SingularAttribute<FeatureEntity, String> name;
    public static volatile SingularAttribute<FeatureEntity, String> type;
    public static volatile ListAttribute<FeatureEntity, GameFeatureEntity> gameFeatures;
}
