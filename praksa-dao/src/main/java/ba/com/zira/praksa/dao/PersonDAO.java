package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
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

}
