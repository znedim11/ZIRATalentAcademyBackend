package ba.com.zira.praksa.dao.model;

import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LinkMapEntity.class)
public abstract class LinkMapEntity_ {
    public static volatile SingularAttribute<LinkMapEntity, String> uuid;
    public static volatile SingularAttribute<LinkMapEntity, LocalDateTime> created;
    public static volatile SingularAttribute<LinkMapEntity, String> createdBy;
    public static volatile SingularAttribute<LinkMapEntity, LocalDateTime> modified;
    public static volatile SingularAttribute<LinkMapEntity, CharacterEntity> character;
    public static volatile SingularAttribute<LinkMapEntity, ConceptEntity> concept;
    public static volatile SingularAttribute<LinkMapEntity, GameEntity> game;
    public static volatile SingularAttribute<LinkMapEntity, LocationEntity> location;
    public static volatile SingularAttribute<LinkMapEntity, ObjectEntity> object;
    public static volatile SingularAttribute<LinkMapEntity, PersonEntity> person;

}
