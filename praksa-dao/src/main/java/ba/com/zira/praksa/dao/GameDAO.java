package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.dao.model.GameEntity;

@Repository
public class GameDAO extends AbstractDAO<GameEntity, Long> {

    public List<GameEntity> getGamesByFeature(final Long featureId) {
        String jpql = "SELECT g FROM GameEntity g, GameFeatureEntity gf, FeatureEntity f WHERE gf.feature.id = :featureId AND f.id = gf.feature.id AND g.id = gf.game.id";

        TypedQuery<GameEntity> query = entityManager.createQuery(jpql, GameEntity.class).setParameter("featureId", featureId);

        return query.getResultList();
    }

    public List<GameEntity> findbyFeatures(final List<Long> features) {
        String jpql = "select distinct g from GameEntity g, GameFeatureEntity gf where gf.feature.id in :ids AND gf.game.id = game.id";

        TypedQuery<GameEntity> query = entityManager.createQuery(jpql, GameEntity.class).setParameter("ids", features);

        return query.getResultList();
    }
}