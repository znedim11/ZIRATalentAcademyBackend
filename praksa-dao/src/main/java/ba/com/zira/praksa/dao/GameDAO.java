package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.praksa.dao.model.GameEntity;

@Repository
public class GameDAO extends AbstractDAO<GameEntity, Long> {

    public PagedData<GameEntity> getGamesByFeature(final Long featureId) {
        String jpql = "SELECT g FROM GameEntity g, GameFeatureEntity gf WHERE gf.feature.id = :featureId AND gf.game.id = g.id";

        TypedQuery<GameEntity> query = entityManager.createQuery(jpql, GameEntity.class).setParameter("featureId", featureId);

        PagedData<GameEntity> gamesPagedData = new PagedData<GameEntity>();
        gamesPagedData.setRecords(query.getResultList());

        return gamesPagedData;
    }

    public List<GameEntity> findbyFeatures(final List<Long> features) {
        String jpql = "SELECT DISTINCT g FROM GameEntity g, GameFeatureEntity gf WHERE gf.feature.id IN :ids AND gf.game.id = game.id";

        TypedQuery<GameEntity> query = entityManager.createQuery(jpql, GameEntity.class).setParameter("ids", features);

        return query.getResultList();
    }
}