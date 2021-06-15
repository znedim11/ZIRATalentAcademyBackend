package ba.com.zira.praksa.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.dao.model.ConceptEntity;
import ba.com.zira.praksa.dao.model.GameEntity;

@Repository
public class ConceptDAO extends AbstractDAO<ConceptEntity, Long> {
    public List<GameEntity> getGamesByLocation(final Long conceptId) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(
                "SELECT g FROM GameEntity g, ConceptEntity c, LinkMapEntity lm WHERE l.id = %d AND lm.location.id = l.id AND lm.game.id = g.id",
                conceptId));

        return entityManager.createQuery(stringBuilder.toString(), GameEntity.class).getResultList();

    }
}
