package ba.com.zira.praksa.dao;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.commons.model.Filter;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.commons.model.PaginationFilter;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.dao.model.GameEntity;

@Repository
public class LoVDAO extends AbstractDAO<GameEntity, Long> {

    public PagedData<LoV> handlePaginationFilter(final Filter filter, final CriteriaQuery<LoV> criteriaQuery, String entityName) {
        TypedQuery<LoV> query = entityManager.createQuery(criteriaQuery);
        PaginationFilter paginationFilter = filter.getPaginationFilter();
        PagedData<LoV> pagedData = new PagedData<>();
        if (paginationFilter != null && paginationFilter.getPage() >= 0 && paginationFilter.getEntitiesPerPage() > 0) {
            int numberOfRecords = countAll(filter, criteriaQuery, entityName);
            pagedData.setPage(paginationFilter.getPage());
            pagedData.setRecordsPerPage(paginationFilter.getEntitiesPerPage());
            pagedData.setNumberOfPages((int) Math.ceil((float) numberOfRecords / paginationFilter.getEntitiesPerPage()));
            pagedData.setNumberOfRecords(numberOfRecords);
            query.setFirstResult((pagedData.getPage() - 1) * pagedData.getRecordsPerPage());
            query.setMaxResults(pagedData.getRecordsPerPage());
        }
        pagedData.setRecords(query.getResultList());
        return pagedData;
    }

    public int countAll(Filter filter, CriteriaQuery<LoV> criteriaQuery, String entityName) {
        TypedQuery<Long> q = entityManager.createQuery(String.format("SELECT COUNT(id) FROM %s", entityName), Long.class);
        Long count = q.getSingleResult();
        return count.intValue();
    }
}
