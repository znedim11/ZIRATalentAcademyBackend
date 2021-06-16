package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.dao.model.ConceptEntity;
import ba.com.zira.praksa.dao.model.GameEntity;
import ba.com.zira.praksa.dao.model.PersonEntity;

@Repository
public class ConceptDAO extends AbstractDAO<ConceptEntity, Long> {
    public List<GameEntity> getGamesByConcept(final Long conceptId) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT g FROM GameEntity g, LinkMapEntity lm WHERE lm.concept.id = :cId AND lm.game.id = g.id");

        TypedQuery<GameEntity> query = entityManager.createQuery(stringBuilder.toString(), GameEntity.class);
        query.setParameter("cId", conceptId);

        return query.getResultList();

    }

    public List<PersonEntity> getPersonsByConcept(final Long conceptId) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT p FROM PersonEntity p, LinkMapEntity lm WHERE lm.concept.id = :cId AND lm.person.id = p.id");

        TypedQuery<PersonEntity> query = entityManager.createQuery(stringBuilder.toString(), PersonEntity.class);
        query.setParameter("cId", conceptId);

        return query.getResultList();

    }

    public List<LoV> getLoVs(List<Long> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("SELECT new ba.com.zira.praksa.api.model.LoV(c.id, c.name) FROM ConceptEntity c %s",
                list != null ? "WHERE c.id IN :list" : ""));

        TypedQuery<LoV> query = entityManager.createQuery(stringBuilder.toString(), LoV.class);
        if (list != null) {
            query.setParameter("list", list);
        }

        return query.getResultList();
    }
}
