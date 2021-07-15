package ba.com.zira.praksa.dao;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.enums.ReleaseType;
import ba.com.zira.praksa.dao.model.ReleaseEntity;

@Repository
public class ReleaseDAO extends AbstractDAO<ReleaseEntity, String> {

    public List<ReleaseEntity> getReleasesPerTimetable(LocalDateTime startRange, LocalDateTime endRange, String releaseType) {

        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT r FROM ReleaseEntity r WHERE releaseDate >= :startDate AND releaseDate <= :endDate");

        if (ReleaseType.GAME.getValue().equalsIgnoreCase(releaseType) || ReleaseType.PLATFORM.getValue().equalsIgnoreCase(releaseType)) {
            queryString.append(" AND type = :releaseType");
        }

        TypedQuery<ReleaseEntity> query = entityManager.createQuery(queryString.toString(), ReleaseEntity.class);

        if (ReleaseType.BOTH.getValue().equalsIgnoreCase(releaseType) || releaseType == null || releaseType.isEmpty()) {

            query.setParameter("startDate", startRange);
            query.setParameter("endDate", endRange);

        } else {
            query.setParameter("startDate", startRange);
            query.setParameter("endDate", endRange);
            query.setParameter("releaseType", releaseType.toUpperCase());

        }

        return query.getResultList();
    }

}