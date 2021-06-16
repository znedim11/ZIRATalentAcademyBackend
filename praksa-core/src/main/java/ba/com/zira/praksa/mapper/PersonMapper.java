package ba.com.zira.praksa.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import ba.com.zira.praksa.api.model.person.Person;
import ba.com.zira.praksa.dao.model.PersonEntity;

/**
 * @author zira
 *
 */

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonEntity dtoToEntity(Person dto);

    Person entityToDto(PersonEntity entity);

    List<Person> entityListToDtoList(List<PersonEntity> entityList);
}
