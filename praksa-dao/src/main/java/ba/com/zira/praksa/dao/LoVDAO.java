package ba.com.zira.praksa.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.commons.model.Filter;
import ba.com.zira.commons.model.FilterExpression;
import ba.com.zira.commons.model.FilterExpression.FilterOperation;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.commons.model.PaginationFilter;
import ba.com.zira.commons.utils.FilterUtils;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.dao.model.GameEntity;

@Repository
public class LoVDAO extends AbstractDAO<GameEntity, Long> {

    public PagedData<LoV> handlePaginationFilter(final Filter filter, final CriteriaQuery<LoV> criteriaQuery, Class<?> entityClass) {
        TypedQuery<LoV> query = entityManager.createQuery(criteriaQuery);

        PagedData<LoV> pagedData = new PagedData<>();
        int numberOfRecords = countAll(filter, entityClass);

        if (filter != null) {
            PaginationFilter paginationFilter = filter.getPaginationFilter();
            if (paginationFilter != null && paginationFilter.getPage() >= 0 && paginationFilter.getEntitiesPerPage() > 0) {
                pagedData.setPage(paginationFilter.getPage());
                pagedData.setRecordsPerPage(paginationFilter.getEntitiesPerPage());
                pagedData.setNumberOfPages((int) Math.ceil((float) numberOfRecords / paginationFilter.getEntitiesPerPage()));
                pagedData.setNumberOfRecords(numberOfRecords);
                query.setFirstResult((pagedData.getPage() - 1) * pagedData.getRecordsPerPage());
                query.setMaxResults(pagedData.getRecordsPerPage());
            } else {
                pagedData.setPage(1);
                pagedData.setRecordsPerPage(numberOfRecords);
                pagedData.setNumberOfPages(1);
                pagedData.setNumberOfRecords(numberOfRecords);
            }
        } else {
            pagedData.setPage(1);
            pagedData.setRecordsPerPage(numberOfRecords);
            pagedData.setNumberOfPages(1);
            pagedData.setNumberOfRecords(numberOfRecords);
        }
        pagedData.setRecords(query.getResultList());
        return pagedData;
    }

    @Override
    public void handleFilterExpressions(final Filter filter, final CriteriaQuery<?> criteriaQuery) {
        if (filter != null) {
            List<FilterExpression> filterExpressions = filter.getFilterExpressions();

            List<Predicate> restrictions = new ArrayList<>();
            if (criteriaQuery.getRestriction() != null) {
                restrictions.add(criteriaQuery.getRestriction());
            }

            if (filterExpressions != null && !filterExpressions.isEmpty()) {
                for (FilterExpression filterExpression : filterExpressions) {

                    filterExpression = FilterUtils.convertExpresionValueToObject(criteriaQuery, filterExpression);

                    if (FilterOperation.EQUALS == filterExpression.getFilterOperation()) {
                        restrictions.add(FilterUtils.addEquals(builder, filterExpression, criteriaQuery));
                    } else if (FilterOperation.NOT_EQUALS == filterExpression.getFilterOperation()) {
                        restrictions.add(FilterUtils.addNotEquals(builder, filterExpression, criteriaQuery));
                    } else if (FilterOperation.BEGINS_WITH == filterExpression.getFilterOperation()) {
                        restrictions.add(FilterUtils.addBeginsWith(builder, filterExpression, criteriaQuery));
                    } else if (FilterOperation.ENDS_WITH == filterExpression.getFilterOperation()) {
                        restrictions.add(FilterUtils.addEndsWith(builder, filterExpression, criteriaQuery));
                    } else if (FilterOperation.CONTAINS == filterExpression.getFilterOperation()) {
                        restrictions.add(FilterUtils.addContains(builder, filterExpression, criteriaQuery));
                    } else if (FilterOperation.DOES_NOT_CONTAIN == filterExpression.getFilterOperation()) {
                        restrictions.add(FilterUtils.addDoesNotContain(builder, filterExpression, criteriaQuery));
                    } else if (FilterOperation.IN == filterExpression.getFilterOperation()) {
                        restrictions.add(FilterUtils.addOrs(builder, filterExpression, criteriaQuery));
                    } else if (FilterOperation.NOT_IN == filterExpression.getFilterOperation()) {
                        restrictions.addAll(FilterUtils.addNotEqualsList(builder, filterExpression, criteriaQuery));
                    } else if (FilterOperation.GREATER_THAN == filterExpression.getFilterOperation()) {
                        FilterUtils.addGreaterThen(restrictions, builder, filterExpression, criteriaQuery);
                    } else if (FilterOperation.GREATER_THAN_EQUALS == filterExpression.getFilterOperation()) {
                        FilterUtils.addGreaterThenOrEquals(restrictions, builder, filterExpression, criteriaQuery);
                    } else if (FilterOperation.LESS_THAN == filterExpression.getFilterOperation()) {
                        FilterUtils.addLessThen(restrictions, builder, filterExpression, criteriaQuery);
                    } else if (FilterOperation.LESS_THAN_EQUALS == filterExpression.getFilterOperation()) {
                        FilterUtils.addLessThenOrEquals(restrictions, builder, filterExpression, criteriaQuery);
                    } else if (FilterOperation.BETWEEN == filterExpression.getFilterOperation()) {
                        FilterUtils.addBetween(restrictions, builder, filterExpression, criteriaQuery);
                    }
                }
                criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
            }
        }
    }

    public int countAll(Filter filter, Class<?> entityClass) {

        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<?> root = query.from(entityClass);

        query.select(builder.count(root));
        handleFilterExpressions(filter, query);

        return entityManager.createQuery(query).getSingleResult() == null ? 0
                : entityManager.createQuery(query).getSingleResult().intValue();
    }

}
