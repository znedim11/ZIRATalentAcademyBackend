package ba.com.zira.praksa.dao;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.dao.model.PersonEntity;

@Repository
public class PersonDAO extends AbstractDAO<PersonEntity, Long> {

}
