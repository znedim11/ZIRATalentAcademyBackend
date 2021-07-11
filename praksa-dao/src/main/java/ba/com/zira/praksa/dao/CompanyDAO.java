package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.game.dlc.DlcCompany;
import ba.com.zira.praksa.dao.model.CompanyEntity;

@Repository
public class CompanyDAO extends AbstractDAO<CompanyEntity, Long> {

    public List<DlcCompany> getDlcCompanies() {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT new ba.com.zira.praksa.api.model.game.dlc.DlcCompany(company.id, company.name, ");
        jpql.append("SUM((SELECT COUNT(*) FROM ReleaseEntity r WHERE r.publisher.id = company.id AND r.game.id = game.id)), ");
        jpql.append("SUM((SELECT COUNT(*) FROM ReleaseEntity r WHERE r.developer.id = company.id AND r.game.id = game.id)), ");
        jpql.append("MIN(release.releaseDate)) ");
        jpql.append("FROM CompanyEntity company ");
        jpql.append("JOIN ReleaseEntity release ON release.developer.id = company.id ");
        jpql.append("JOIN GameEntity game ON game.id = release.game.id ");
        jpql.append("WHERE game.dlc like '1' AND game.parentGame IS NOT NULL ");
        jpql.append("GROUP BY 1, 2");

        TypedQuery<DlcCompany> query = entityManager.createQuery(jpql.toString(), DlcCompany.class);
        return query.getResultList();
    }

}
