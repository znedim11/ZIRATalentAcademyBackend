package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.dao.model.RegionEntity;

@Repository
public class RegionDAO extends AbstractDAO<RegionEntity, Long> {
    public List<LoV> findNamesForIds(List<Long> ids) {
        String jpql = String.format("select new ba.com.zira.praksa.api.model.LoV(r.id, r.name) from RegionEntity r %s",
                ids != null ? "where r.id in :ids" : "");
        TypedQuery<LoV> regions = entityManager.createQuery(jpql, LoV.class);
        if (ids != null) {
            regions.setParameter("ids", ids);
        }
        return regions.getResultList();
    }

}