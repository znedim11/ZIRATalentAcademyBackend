package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.api.model.game.dlc.DlcFranchise;
import ba.com.zira.praksa.dao.model.FranchiseEntity;

@Repository
public class FranchiseDAO extends AbstractDAO<FranchiseEntity, Long> {

    public List<DlcFranchise> getDlcFranchises(String dlc) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT new ba.com.zira.praksa.api.model.game.dlc.DlcFranchise(franchise.id, franchise.name, ");
        jpql.append("SUM((SELECT COUNT(*) FROM GameEntity dlc WHERE dlc.parentGame = game.id)), MIN(r.releaseDate)) ");
        jpql.append("FROM FranchiseEntity franchise ");
        jpql.append("JOIN GameEntity game ON franchise.id = game.franchise.id ");
        jpql.append("LEFT JOIN ReleaseEntity r ON game.id = r.game.id ");
        jpql.append("WHERE game.dlc like :dlc AND (SELECT COUNT(*) FROM GameEntity dlc WHERE dlc.parentGame = game.id) <> 0 ");
        jpql.append("GROUP BY 1");

        TypedQuery<DlcFranchise> query = entityManager.createQuery(jpql.toString(), DlcFranchise.class);
        query.setParameter("dlc", dlc);

        return query.getResultList();
    }

    public List<LoV> getLoVs(List<Long> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("SELECT new ba.com.zira.praksa.api.model.LoV(f.id, f.name) FROM FranchiseEntity f %s",
                list != null ? "WHERE f.id IN :list" : ""));

        TypedQuery<LoV> query = entityManager.createQuery(stringBuilder.toString(), LoV.class);
        if (list != null) {
            query.setParameter("list", list);
        }

        return query.getResultList();
    }

}
