package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.dao.model.CharacterEntity;
import ba.com.zira.praksa.dao.model.ConceptEntity;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.dao.model.ObjectEntity;
import ba.com.zira.praksa.dao.model.PersonEntity;

@Repository
public class PersonDAO extends AbstractDAO<PersonEntity, Long> {

    static final String PERSON_ID = "personId";

    public List<GameEntity> getGamesForPerson(final Long personId) {

        TypedQuery<GameEntity> query = entityManager.createQuery(
                "SELECT g FROM GameEntity g, LinkMapEntity lm WHERE lm.person.id= :personId AND lm.game.id= g.id", GameEntity.class);

        query.setParameter(PERSON_ID, personId);
        return query.getResultList();

    }

    public List<ConceptEntity> getConceptsForPerson(final Long personId) {

        TypedQuery<ConceptEntity> query = entityManager.createQuery(
                "SELECT c FROM ConceptEntity c, LinkMapEntity lm WHERE lm.person.id= :personId AND lm.concept.id= c.id",
                ConceptEntity.class);

        query.setParameter(PERSON_ID, personId);
        return query.getResultList();

    }

    public List<ObjectEntity> getObjectsForPerson(final Long personId) {

        TypedQuery<ObjectEntity> query = entityManager.createQuery(
                "SELECT o FROM ObjectEntity o, LinkMapEntity lm WHERE lm.person.id= :personId AND lm.object.id= c.id", ObjectEntity.class);

        query.setParameter(PERSON_ID, personId);
        return query.getResultList();

    }

    public List<CharacterEntity> getCharactersForPerson(final Long personId) {

        TypedQuery<CharacterEntity> query = entityManager.createQuery(
                "SELECT c FROM CharacterEntity c, LinkMapEntity lm WHERE lm.person.id= :personId AND lm.character.id= c.id",
                CharacterEntity.class);

        query.setParameter(PERSON_ID, personId);
        return query.getResultList();

    }

    public List<LoV> getLoVs(List<Long> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(
                "SELECT new ba.com.zira.praksa.api.model.LoV(p.id, CONCAT(p.firstName,' ',p.lastName)) FROM PersonEntity p %s",
                list != null ? "WHERE p.id IN :list" : ""));

        TypedQuery<LoV> query = entityManager.createQuery(stringBuilder.toString(), LoV.class);
        if (list != null) {
            query.setParameter("list", list);
        }

        return query.getResultList();
    }

    public List<LoV> getLoVsNotConnectedTo(String field, String idField, Long id) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.format(
                "SELECT DISTINCT p.id FROM PersonEntity p LEFT OUTER JOIN LinkMapEntity lm ON lm.person.id= p.id WHERE lm.%s.%s = :id",
                field, idField));

        TypedQuery<Long> listQuery = entityManager.createQuery(stringBuilder.toString(), Long.class);
        listQuery.setParameter("id", id);
        List<Long> list = listQuery.getResultList();

        stringBuilder.setLength(0);
        stringBuilder.append(String.format(
                "SELECT new ba.com.zira.praksa.api.model.LoV(p.id, CONCAT(p.firstName,' ',p.lastName)) FROM PersonEntity p %s",
                !list.isEmpty() ? "WHERE p.id NOT IN :list" : ""));

        TypedQuery<LoV> query = entityManager.createQuery(stringBuilder.toString(), LoV.class);
        if (!list.isEmpty()) {
            query.setParameter("list", list);
        }

        return query.getResultList();
    }
}
