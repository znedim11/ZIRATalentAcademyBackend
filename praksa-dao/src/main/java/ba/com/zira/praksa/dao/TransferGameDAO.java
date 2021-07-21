package ba.com.zira.praksa.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.datatransfer.TransferGameHelper;
import ba.com.zira.praksa.dao.model.TransferGameEntity;

@Repository
public class TransferGameDAO extends AbstractDAO<TransferGameEntity, Long> {
    public List<TransferGameHelper> findAllHelper(int pageSize, int pageNumber) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(
                "SELECT new ba.com.zira.praksa.api.model.datatransfer.TransferGameHelper(g.name, g.overview, g.cooperative, g.communityRating, g.platform, g.genres,g.developer, g.publisher, g.releaseDate, g.releaseYear, g.videoUrl, g.maxPlayers,g.releaseType, g.wikipediaUrl) FROM TransferGameEntity g ORDER BY g.name");

        TypedQuery<TransferGameHelper> query = entityManager.createQuery(stringBuilder.toString(), TransferGameHelper.class);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Long getCount() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT COUNT(g) FROM TransferGameEntity g");

        TypedQuery<Long> query = entityManager.createQuery(stringBuilder.toString(), Long.class);

        return query.getSingleResult();
    }
}