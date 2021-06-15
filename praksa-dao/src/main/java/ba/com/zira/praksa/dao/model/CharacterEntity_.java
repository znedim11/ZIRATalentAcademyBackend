package ba.com.zira.praksa.dao.model;

import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CharacterEntity.class)
public abstract class CharacterEntity_ {
    public static volatile SingularAttribute<CharacterEntity, Long> id;
    public static volatile SingularAttribute<CharacterEntity, String> aliases;
    public static volatile SingularAttribute<CharacterEntity, LocalDateTime> created;
    public static volatile SingularAttribute<CharacterEntity, String> createdBy;
    public static volatile SingularAttribute<CharacterEntity, LocalDateTime> modified;
    public static volatile SingularAttribute<CharacterEntity, String> modifiedBy;
    public static volatile SingularAttribute<CharacterEntity, LocalDateTime> dob;
    public static volatile SingularAttribute<CharacterEntity, LocalDateTime> dod;
    public static volatile SingularAttribute<CharacterEntity, String> gender;
    public static volatile SingularAttribute<CharacterEntity, String> information;
    public static volatile SingularAttribute<CharacterEntity, String> name;
    public static volatile SingularAttribute<CharacterEntity, String> realName;
    public static volatile ListAttribute<CharacterEntity, LinkMapEntity> linkMaps;

}
