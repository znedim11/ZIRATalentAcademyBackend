package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.company.report.CompanyRegionPlatform;
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

    public List<CompanyRegionPlatform> getRegionReportByCompanies(final List<Long> companies, final Boolean publisher) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(
                "SELECT new ba.com.zira.praksa.api.model.company.report.CompanyRegionPlatform(COUNT(*), min(r.releaseDate), g.fullName, c.id, region.id, region.name) ");
        jpql.append("FROM RegionEntity region ");
        jpql.append("JOIN ReleaseEntity r ON region.id = r.platform.id ");
        if (publisher) {
            jpql.append("JOIN CompanyEntity c ON r.publisher.id = c.id ");
        } else {
            jpql.append("JOIN CompanyEntity c ON r.developer.id = c.id ");
        }
        jpql.append("JOIN GameEntity g ON g.id = r.game.id ");
        jpql.append("WHERE c.id IN :ids ");
        jpql.append("GROUP BY 3, 4, 5 ");

        TypedQuery<CompanyRegionPlatform> query = entityManager.createQuery(jpql.toString(), CompanyRegionPlatform.class)
                .setParameter("ids", companies);

        return query.getResultList();
    }

}