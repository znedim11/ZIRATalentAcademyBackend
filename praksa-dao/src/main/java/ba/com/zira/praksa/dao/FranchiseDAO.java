package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.game.dlc.DlcFranchise;
import ba.com.zira.praksa.dao.model.FranchiseEntity;

@Repository
public class FranchiseDAO extends AbstractDAO<FranchiseEntity, Long> {

    public List<DlcFranchise> getDlcFranchises() {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT new ba.com.zira.praksa.api.model.game.dlc.DlcFranchise(franchise.id, franchise.name, ");
        jpql.append("(SELECT COUNT(*) FROM GameEntity dlc WHERE dlc.parentGame = game.id), MIN(r.releaseDate)) ");
        jpql.append("FROM FranchiseEntity franchise ");
        jpql.append("JOIN GameEntity game ON franchise.id = game.franchise.id ");
        jpql.append("JOIN ReleaseEntity r ON game.id = r.game.id ");
        jpql.append("WHERE game.dlc like '0' AND (SELECT COUNT(*) FROM GameEntity dlc WHERE dlc.parentGame = game.id) <> 0 ");
        jpql.append("GROUP BY 1, 2, 3");

        TypedQuery<DlcFranchise> query = entityManager.createQuery(jpql.toString(), DlcFranchise.class);
        return query.getResultList();
    }

}