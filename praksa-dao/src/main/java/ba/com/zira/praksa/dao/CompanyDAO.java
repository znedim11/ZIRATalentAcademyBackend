package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.commons.model.Filter;
import ba.com.zira.commons.model.PagedData;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.company.CompanySearchRequest;
import ba.com.zira.praksa.api.model.company.CompanySearchResponse;
import ba.com.zira.praksa.api.model.game.dlc.DlcCompany;
import ba.com.zira.praksa.dao.model.CompanyEntity;
import ba.com.zira.praksa.dao.model.CompanyEntity_;

@Repository
public class CompanyDAO extends AbstractDAO<CompanyEntity, Long> {

    LoVDAO loVDAO;

    public CompanyDAO(LoVDAO loVDAO) {
        super();
        this.loVDAO = loVDAO;
    }

    public List<DlcCompany> getDlcCompanies(String dlc) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT new ba.com.zira.praksa.api.model.game.dlc.DlcCompany(company.id, company.name, ");
        jpql.append("(SELECT COUNT(*) FROM ReleaseEntity r WHERE r.publisher.id = company.id AND r.game.id = game.id), ");
        jpql.append("(SELECT COUNT(*) FROM ReleaseEntity r WHERE r.developer.id = company.id AND r.game.id = game.id), ");
        jpql.append("MIN(release.releaseDate)) ");
        jpql.append("FROM CompanyEntity company ");
        jpql.append("JOIN ReleaseEntity release ON release.developer.id = company.id ");
        jpql.append("JOIN GameEntity game ON game.id = release.game.id ");
        jpql.append("WHERE game.dlc like :dlc AND game.parentGame IS NOT NULL ");
        jpql.append("GROUP BY 1, 3, 4");

        TypedQuery<DlcCompany> query = entityManager.createQuery(jpql.toString(), DlcCompany.class);
        query.setParameter("dlc", dlc);

        return query.getResultList();
    }

    public PagedData<LoV> getLoVs(Filter filter) {
        CriteriaQuery<LoV> criteriaQuery = builder.createQuery(LoV.class);
        Root<CompanyEntity> root = criteriaQuery.from(CompanyEntity.class);

        criteriaQuery.multiselect(root.get(CompanyEntity_.id), root.get(CompanyEntity_.name))
                .orderBy(builder.asc(root.get(CompanyEntity_.name)));

        loVDAO.handleFilterExpressions(filter, criteriaQuery);
        return loVDAO.handlePaginationFilter(filter, criteriaQuery, CompanyEntity.class);
    }

    public List<CompanyEntity> getCompaniesByIds(final List<Long> ids) {
        String jpql = "SELECT c FROM CompanyEntity c WHERE c.id IN :ids";

        TypedQuery<CompanyEntity> query = entityManager.createQuery(jpql, CompanyEntity.class).setParameter("ids", ids);

        return query.getResultList();
    }

    public List<CompanySearchResponse> searchCompany(CompanySearchRequest searchRequest) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT new ba.com.zira.praksa.api.model.company.CompanySearchResponse(c.id, c.name, c.outlineText, ms.url)");
        jpql.append(" FROM CompanyEntity c");
        jpql.append(" LEFT OUTER JOIN MediaEntity m ON m.objectId = c.id");
        jpql.append(" LEFT OUTER JOIN MediaStoreEntity ms ON ms.media.id = m.id");
        jpql.append(" WHERE 1=1");

        if (searchRequest.getName() != null && !searchRequest.getName().isEmpty()) {
            jpql.append(" AND UPPER(c.name) like :name");
        }

        if (searchRequest.getDob() != null && searchRequest.getDobCondition() != null && !searchRequest.getDobCondition().isEmpty()) {
            if (searchRequest.getDobCondition().toUpperCase().equals("BEFORE")) {
                System.out.println("BEFORE");
                jpql.append(" AND c.dob < :dob");
            } else if (searchRequest.getDobCondition().toUpperCase().equals("AFTER")) {
                System.out.println("AFTER");
                jpql.append(" AND c.dob > :dob");
            } else {
                System.out.println("EQUAL");
                jpql.append(" AND c.dob = :dob");
            }
        }

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            if (searchRequest.getSortBy().toUpperCase().equals("ALPHABETICAL ORDER")) {
                jpql.append(" ORDER BY c.name ASC");
            } else if (searchRequest.getSortBy().toUpperCase().equals("LAST EDIT")) {
                jpql.append(" ORDER BY c.modified DESC");
            }
        }

        TypedQuery<CompanySearchResponse> query = entityManager.createQuery(jpql.toString(), CompanySearchResponse.class);

        if (searchRequest.getName() != null && !searchRequest.getName().isEmpty()) {
            query.setParameter("name", "%" + searchRequest.getName().toUpperCase() + "%");
        }

        if (searchRequest.getDob() != null) {
            query.setParameter("dob", searchRequest.getDob());

        }

        return query.getResultList();
    }

}
