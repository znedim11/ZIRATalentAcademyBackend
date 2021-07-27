package ba.com.zira.praksa.dao.model;

import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(GameEntity.class)
public abstract class GameEntity_ {
    public static volatile SingularAttribute<GameEntity, Long> id;
    public static volatile SingularAttribute<GameEntity, LocalDateTime> created;
    public static volatile SingularAttribute<GameEntity, String> createdBy;
    public static volatile SingularAttribute<GameEntity, LocalDateTime> modified;
    public static volatile SingularAttribute<GameEntity, String> modifiedBy;
    public static volatile SingularAttribute<GameEntity, String> dlc;
    public static volatile SingularAttribute<GameEntity, String> fullName;
    public static volatile SingularAttribute<GameEntity, String> genre;
    public static volatile SingularAttribute<GameEntity, String> information;
    public static volatile SingularAttribute<GameEntity, String> outlineText;
    public static volatile SingularAttribute<GameEntity, FranchiseEntity> franchise;
    public static volatile SingularAttribute<GameEntity, GameEntity> parentGame;
    public static volatile ListAttribute<GameEntity, ExternalReviewEntity> externalReviews;
    public static volatile ListAttribute<GameEntity, LinkMapEntity> linkMaps;
    public static volatile ListAttribute<GameEntity, GameEntity> dlcGames;
    public static volatile ListAttribute<GameEntity, GameFeatureEntity> gameFeatures;
    public static volatile ListAttribute<GameEntity, ReleaseEntity> releases;
    public static volatile ListAttribute<GameEntity, ReviewEntity> reviews;
}
