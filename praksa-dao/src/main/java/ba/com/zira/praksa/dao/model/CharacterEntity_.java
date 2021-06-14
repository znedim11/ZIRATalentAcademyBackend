package ba.com.zira.praksa.dao.model;

import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CharacterEntity.class)
public abstract class CharacterEntity_ {
    public static volatile SingularAttribute<CharacterEntity, Long> id;
    public static volatile SingularAttribute<CharacterEntity, LocalDateTime> created;
    public static volatile SingularAttribute<CharacterEntity, String> createdBy;
    public static volatile SingularAttribute<CharacterEntity, LocalDateTime> modified;
    // public static volatile SingularAttribute<CharacterEntity,
    // CharacterEntity> character;
    // public static volatile SingularAttribute<CharacterEntity, ConceptEntity>
    // concept;
    // public static volatile SingularAttribute<CharacterEntity, GameEntity>
    // game;
    // public static volatile SingularAttribute<CharacterEntity, LocationEntity>
    // location;
    // public static volatile SingularAttribute<CharacterEntity, ObjectEntity>
    // object;
    // public static volatile SingularAttribute<CharacterEntity, PersonEntity>
    // person;

}
