package ba.com.zira.praksa.dao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.dao.model.PlatformEntity;

@Repository
public class PlatformDAO extends AbstractDAO<PlatformEntity, Long> {

    public Map<Long, String> getPlatformNames(final List<Long> ids) {
        String jpql = "select new ba.com.zira.praksa.api.model.LoV(p.id, p.fullName) from PlatformEntity p where p.id in :ids";
        TypedQuery<LoV> query = entityManager.createQuery(jpql, LoV.class).setParameter("ids", ids);
        List<LoV> lovs = query.getResultList();
        return lovs.stream().collect(Collectors.toMap(LoV::getId, LoV::getName));
    }
}