package ba.com.zira.praksa.dao.model;

import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PersonEntity.class)
public abstract class PersonEntity_ {
    public static volatile SingularAttribute<PersonEntity, Long> id;
    public static volatile SingularAttribute<PersonEntity, String> aliases;
    public static volatile SingularAttribute<PersonEntity, String> city;
    public static volatile SingularAttribute<PersonEntity, String> country;
    public static volatile SingularAttribute<PersonEntity, LocalDateTime> created;
    public static volatile SingularAttribute<PersonEntity, String> createdBy;
    public static volatile SingularAttribute<PersonEntity, LocalDateTime> dob;
    public static volatile SingularAttribute<PersonEntity, LocalDateTime> dod;
    public static volatile SingularAttribute<PersonEntity, String> email;
    public static volatile SingularAttribute<PersonEntity, String> firstName;
    public static volatile SingularAttribute<PersonEntity, String> gender;
    public static volatile SingularAttribute<PersonEntity, String> information;
    public static volatile SingularAttribute<PersonEntity, String> lastName;
    public static volatile SingularAttribute<PersonEntity, LocalDateTime> modified;
    public static volatile SingularAttribute<PersonEntity, String> modifiedBy;
    public static volatile SingularAttribute<PersonEntity, String> twitter;
    public static volatile SingularAttribute<PersonEntity, String> website;
    public static volatile ListAttribute<PersonEntity, LinkMapEntity> linkMaps;

}
