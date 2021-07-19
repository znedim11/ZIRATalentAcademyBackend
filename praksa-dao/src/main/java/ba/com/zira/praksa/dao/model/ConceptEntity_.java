package ba.com.zira.praksa.dao.model;

import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ConceptEntity.class)
public abstract class ConceptEntity_ {
    public static volatile SingularAttribute<ConceptEntity, Long> id;
    public static volatile SingularAttribute<ConceptEntity, String> aliases;
    public static volatile SingularAttribute<ConceptEntity, LocalDateTime> created;
    public static volatile SingularAttribute<ConceptEntity, String> createdBy;
    public static volatile SingularAttribute<ConceptEntity, String> information;
    public static volatile SingularAttribute<ConceptEntity, LocalDateTime> modified;
    public static volatile SingularAttribute<ConceptEntity, String> modifiedBy;
    public static volatile SingularAttribute<ConceptEntity, String> name;
    public static volatile SingularAttribute<ConceptEntity, String> outline;
    public static volatile ListAttribute<ConceptEntity, LinkMapEntity> linkMaps;

}
