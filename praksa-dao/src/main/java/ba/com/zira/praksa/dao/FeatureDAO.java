package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.dao.model.FeatureEntity;

@Repository
public class FeatureDAO extends AbstractDAO<FeatureEntity, Long> {

    public List<FeatureEntity> findbyIds(final List<Long> featureIds) {
        String jpql = "SELECT f FROM FeatureEntity f WHERE f.id IN :ids";

        TypedQuery<FeatureEntity> query = entityManager.createQuery(jpql, FeatureEntity.class).setParameter("ids", featureIds);

        return query.getResultList();
    }

    public List<FeatureEntity> getFeaturesByGame(final Long gameId) {
        String jpql = "SELECT f FROM FeatureEntity f, GameFeatureEntity gf, GameEntity g WHERE gf.game.id = :gameId AND g.id = gf.game.id AND f.id = gf.feature.id";

        TypedQuery<FeatureEntity> query = entityManager.createQuery(jpql, FeatureEntity.class).setParameter("gameId", gameId);

        return query.getResultList();
    }

    public List<LoV> getLoVs(List<Long> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("SELECT new ba.com.zira.praksa.api.model.LoV(f.id, f.name) FROM FeatureEntity f %s",
                list != null ? "WHERE f.id IN :list" : ""));

        TypedQuery<LoV> query = entityManager.createQuery(stringBuilder.toString(), LoV.class);
        if (list != null) {
            query.setParameter("list", list);
        }

        return query.getResultList();
    }
}