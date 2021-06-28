package ba.com.zira.praksa.dao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.dao.model.GameEntity;

@Repository
public class GameDAO extends AbstractDAO<GameEntity, Long> {

    public PagedData<GameEntity> getGamesByFeature(final Long featureId) {
        String jpql = "SELECT g FROM GameEntity g, GameFeatureEntity gf WHERE gf.feature.id = :featureId AND gf.game.id = g.id";

        TypedQuery<GameEntity> query = entityManager.createQuery(jpql, GameEntity.class).setParameter("featureId", featureId);

        PagedData<GameEntity> gamesPagedData = new PagedData<>();
        gamesPagedData.setRecords(query.getResultList());

        return gamesPagedData;
    }

    public List<GameEntity> findbyFeatures(final List<Long> features) {
        String jpql = "SELECT DISTINCT g FROM GameEntity g, GameFeatureEntity gf WHERE gf.feature.id IN :ids AND gf.game.id = game.id";

        TypedQuery<GameEntity> query = entityManager.createQuery(jpql, GameEntity.class).setParameter("ids", features);

        return query.getResultList();
    }

    public Map<Long, String> getGameName(final List<Long> ids) {
        String jpql = "select new ba.com.zira.praksa.api.model.LoV(g.id, g.fullName) from GameEntity g where g.id in :ids";
        TypedQuery<LoV> query = entityManager.createQuery(jpql, LoV.class).setParameter("ids", ids);
        List<LoV> lovs = query.getResultList();
        return lovs.stream().collect(Collectors.toMap(LoV::getId, LoV::getName));
    }
}