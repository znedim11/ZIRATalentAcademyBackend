package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.dao.model.GameFeatureEntity;

@Repository
public class GameFeatureDAO extends AbstractDAO<GameFeatureEntity, String> {

    public List<GameFeatureEntity> findbyFeatures(final List<Long> features) {
        String jpql = "SELECT gf FROM GameFeatureEntity gf WHERE gf.feature.id IN :ids ";

        TypedQuery<GameFeatureEntity> query = entityManager.createQuery(jpql, GameFeatureEntity.class).setParameter("ids", features);

        return query.getResultList();
    }

    public boolean checkIfRelationExists(final Long gameId, final Long featureId) {
        String jpql = "SELECT gf FROM GameFeatureEntity gf WHERE gf.game.id = :gameId AND gf.feature.id = :featureId ";

        TypedQuery<GameFeatureEntity> query = entityManager.createQuery(jpql, GameFeatureEntity.class).setParameter("gameId", gameId)
                .setParameter("featureId", featureId);

        return !query.getResultList().isEmpty();
    }
}
