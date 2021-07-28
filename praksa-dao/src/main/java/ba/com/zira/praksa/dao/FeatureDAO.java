package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.commons.model.Filter;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.dao.model.FeatureEntity;
import ba.com.zira.praksa.dao.model.FeatureEntity_;
import ba.com.zira.praksa.dao.model.GameFeatureEntity;

@Repository
public class FeatureDAO extends AbstractDAO<FeatureEntity, Long> {

    LoVDAO loVDAO;

    public FeatureDAO(LoVDAO loVDAO) {
        super();
        this.loVDAO = loVDAO;
    }

    public List<FeatureEntity> findbyIds(final List<Long> featureIds) {
        String jpql = "SELECT f FROM FeatureEntity f WHERE f.id IN :ids";

        TypedQuery<FeatureEntity> query = entityManager.createQuery(jpql, FeatureEntity.class).setParameter("ids", featureIds);

        return query.getResultList();
    }

    public PagedData<FeatureEntity> getFeaturesByGame(final Long gameId) {
        String jpql = "SELECT f FROM FeatureEntity f, GameFeatureEntity gf WHERE gf.game.id = :gameId AND gf.feature.id = f.id";

        TypedQuery<FeatureEntity> query = entityManager.createQuery(jpql, FeatureEntity.class).setParameter("gameId", gameId);

        PagedData<FeatureEntity> featuresPagedData = new PagedData<>();
        featuresPagedData.setRecords(query.getResultList());

        return featuresPagedData;
    }

    public List<LoV> getLoVs(List<Long> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("SELECT new ba.com.zira.praksa.api.model.LoV(f.id, f.name) FROM FeatureEntity f %s",
                list != null ? "WHERE f.id IN :list ORDER BY f.name" : ""));

        TypedQuery<LoV> query = entityManager.createQuery(stringBuilder.toString(), LoV.class);
        if (list != null) {
            query.setParameter("list", list);
        }

        return query.getResultList();
    }

    public void deleteRelations(final Long featureId) {
        CriteriaDelete<GameFeatureEntity> criteriaDelete = builder.createCriteriaDelete(GameFeatureEntity.class);
        Root<GameFeatureEntity> root = criteriaDelete.from(GameFeatureEntity.class);

        criteriaDelete.where(builder.equal(root.get("feature"), featureId));

        entityManager.createQuery(criteriaDelete).executeUpdate();
    }

    public PagedData<LoV> getLoVs(Filter filter) {
        CriteriaQuery<LoV> criteriaQuery = builder.createQuery(LoV.class);
        Root<FeatureEntity> root = criteriaQuery.from(FeatureEntity.class);

        criteriaQuery.multiselect(root.get(FeatureEntity_.id), root.get(FeatureEntity_.name))
                .orderBy(builder.asc(root.get(FeatureEntity_.name)));

        loVDAO.handleFilterExpressions(filter, criteriaQuery);
        return loVDAO.handlePaginationFilter(filter, criteriaQuery, FeatureEntity.class);
    }
}