package ba.com.zira.praksa.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import ba.com.zira.praksa.api.model.person.Person;
import ba.com.zira.praksa.api.model.person.PersonCreateRequest;
import ba.com.zira.praksa.api.model.person.PersonUpdateRequest;
import ba.com.zira.praksa.dao.model.PersonEntity;

/**
 * Defined mapper interface for mapping a DTO to Entity model class
 *
 * @author zira
 *
 */

@Mapper(componentModel = "spring")
public interface PersonMapper {

	PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

	@Mapping(target = "modified", ignore = true)
	@Mapping(target = "modifiedBy", ignore = true)
	@Mapping(target = "created", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	PersonEntity personUpdateToPersonEntity(PersonUpdateRequest personUpdateModel);

	PersonUpdateRequest personEntityToPersonUpdate(PersonEntity personEntity);

	@Mapping(target = "created", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "modified", ignore = true)
	@Mapping(target = "modifiedBy", ignore = true)
	PersonEntity personCreateToPersonEntity(PersonCreateRequest personCreateModel);

	PersonCreateRequest personEntityToPersonCreate(PersonEntity personEntity);

	PersonEntity dtoToEntity(Person person);

	@InheritInverseConfiguration(name = "dtoToEntity")
	Person entityToDto(PersonEntity personEntity);

	List<Person> entityListToPersonList(List<PersonEntity> personEntityList);
}
