package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.dao.model.LocationEntity;

@Repository
public class LocationDAO extends AbstractDAO<LocationEntity, Long> {
    public List<LoV> getLoVs(List<Long> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("SELECT new ba.com.zira.praksa.api.model.LoV(l.id, l.name) FROM LocationEntity l %s",
                list != null ? "WHERE l.id IN :list" : ""));

        TypedQuery<LoV> query = entityManager.createQuery(stringBuilder.toString(), LoV.class);
        if (list != null) {
            query.setParameter("list", list);
        }

        return query.getResultList();
    }

    public List<LoV> getLoVsNotConnectedTo(String field, String idField, Long id) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.format(
                "SELECT DISTINCT l.id FROM LocationEntity l LEFT OUTER JOIN LinkMapEntity lm ON lm.location.id= l.id WHERE lm.%s.%s = :id",
                field, idField));

        TypedQuery<Long> listQuery = entityManager.createQuery(stringBuilder.toString(), Long.class);
        listQuery.setParameter("id", id);
        List<Long> list = listQuery.getResultList();

        stringBuilder.setLength(0);
        stringBuilder.append(String.format("SELECT new ba.com.zira.praksa.api.model.LoV(l.id, l.name) FROM LocationEntity l %s",
                !list.isEmpty() ? "WHERE l.id NOT IN :list" : ""));

        TypedQuery<LoV> query = entityManager.createQuery(stringBuilder.toString(), LoV.class);
        if (!list.isEmpty()) {
            query.setParameter("list", list);
        }

        return query.getResultList();
    }

}
