package ba.com.zira.praksa.dao.model;

import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(GameFeatureEntity.class)
public abstract class GameFeatureEntity_ {
    public static volatile SingularAttribute<GameFeatureEntity, String> uuid;
    public static volatile SingularAttribute<GameFeatureEntity, LocalDateTime> created;
    public static volatile SingularAttribute<GameFeatureEntity, String> createdBy;
    public static volatile SingularAttribute<GameFeatureEntity, LocalDateTime> modified;
    public static volatile SingularAttribute<GameFeatureEntity, String> modifiedBy;
    public static volatile SingularAttribute<GameFeatureEntity, FeatureEntity> feature;
    public static volatile SingularAttribute<GameFeatureEntity, GameEntity> game;
}
