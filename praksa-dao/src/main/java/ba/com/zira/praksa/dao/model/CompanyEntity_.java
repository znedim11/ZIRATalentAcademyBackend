package ba.com.zira.praksa.dao.model;

import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CompanyEntity.class)
public abstract class CompanyEntity_ {
    public static volatile SingularAttribute<CompanyEntity, Long> id;
    public static volatile SingularAttribute<CompanyEntity, String> address;
    public static volatile SingularAttribute<CompanyEntity, String> city;
    public static volatile SingularAttribute<CompanyEntity, String> country;
    public static volatile SingularAttribute<CompanyEntity, LocalDateTime> created;
    public static volatile SingularAttribute<CompanyEntity, String> createdBy;
    public static volatile SingularAttribute<CompanyEntity, String> email;
    public static volatile SingularAttribute<CompanyEntity, LocalDateTime> end_date;
    public static volatile SingularAttribute<CompanyEntity, String> information;
    public static volatile SingularAttribute<CompanyEntity, LocalDateTime> modified;
    public static volatile SingularAttribute<CompanyEntity, String> modifiedBy;
    public static volatile SingularAttribute<CompanyEntity, String> name;
    public static volatile SingularAttribute<CompanyEntity, String> outlineText;
    public static volatile SingularAttribute<CompanyEntity, LocalDateTime> startDate;
    public static volatile SingularAttribute<CompanyEntity, String> telNumber;
    public static volatile SingularAttribute<CompanyEntity, String> website;
    public static volatile SingularAttribute<CompanyEntity, LocalDateTime> dod;
    public static volatile SingularAttribute<CompanyEntity, String> gender;
    public static volatile SingularAttribute<CompanyEntity, String> realName;
    public static volatile ListAttribute<CompanyEntity, ReleaseEntity> publisherReleases;
    public static volatile ListAttribute<CompanyEntity, ReleaseEntity> developerReleases;

}
