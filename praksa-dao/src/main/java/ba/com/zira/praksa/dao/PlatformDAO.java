package ba.com.zira.praksa.dao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.company.report.CompanyRegionPlatform;
import ba.com.zira.praksa.api.model.game.dlc.DlcPlatform;
import ba.com.zira.praksa.dao.model.PlatformEntity;

@Repository
public class PlatformDAO extends AbstractDAO<PlatformEntity, Long> {

    public Map<Long, String> getPlatformNames(final List<Long> ids) {
        String jpql = "select new ba.com.zira.praksa.api.model.LoV(p.id, p.fullName) from PlatformEntity p where p.id in :ids";
        TypedQuery<LoV> query = entityManager.createQuery(jpql, LoV.class).setParameter("ids", ids);
        List<LoV> lovs = query.getResultList();
        return lovs.stream().collect(Collectors.toMap(LoV::getId, LoV::getName));
    }

    public List<LoV> getLoVs(List<Long> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("SELECT new ba.com.zira.praksa.api.model.LoV(p.id, p.fullName) FROM PlatformEntity p %s",
                list != null ? "WHERE p.id IN :list" : ""));

        TypedQuery<LoV> query = entityManager.createQuery(stringBuilder.toString(), LoV.class);
        if (list != null) {
            query.setParameter("list", list);
        }

        return query.getResultList();
    }

    public List<DlcPlatform> getDlcPlatforms(String dlc) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(
                "SELECT new ba.com.zira.praksa.api.model.game.dlc.DlcPlatform(platform.id, platform.fullName, platform.code, COUNT(*)) ");
        jpql.append("FROM PlatformEntity platform ");
        jpql.append("JOIN ReleaseEntity release ON platform.id = release.platform.id ");
        jpql.append("JOIN GameEntity dlc ON dlc.id = release.game.id ");
        jpql.append("WHERE dlc.dlc LIKE :dlc AND dlc.parentGame IS NOT NULL AND ");
        jpql.append("(SELECT COUNT(*) FROM ReleaseEntity r WHERE r.game.id = dlc.parentGame AND r.platform.id = platform.id) > 0 ");
        jpql.append("GROUP BY 1");

        TypedQuery<DlcPlatform> query = entityManager.createQuery(jpql.toString(), DlcPlatform.class);
        query.setParameter("dlc", dlc);

        return query.getResultList();
    }

    public List<CompanyRegionPlatform> getPlatformsReportByCompanies(final List<Long> companies, final Boolean publisher) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(
                "SELECT new ba.com.zira.praksa.api.model.company.report.CompanyRegionPlatform(COUNT(*), min(r.releaseDate), g.fullName, c.id, p.id, p.fullName) ");
        jpql.append("FROM PlatformEntity p ");
        jpql.append("JOIN ReleaseEntity r ON p.id = r.platform.id ");
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