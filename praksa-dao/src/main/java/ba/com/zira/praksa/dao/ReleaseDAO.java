package ba.com.zira.praksa.dao;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.dao.model.ReleaseEntity;

@Repository
public class ReleaseDAO extends AbstractDAO<ReleaseEntity, String> {

    public List<ReleaseEntity> getReleasesPerTimetable(LocalDateTime startRange, LocalDateTime endRange) {

        TypedQuery<ReleaseEntity> query = entityManager.createQuery(
                "SELECT r FROM ReleaseEntity r WHERE releaseDate >= :startDate AND releaseDate <= :endDate", ReleaseEntity.class);

        query.setParameter("startDate", startRange);
        query.setParameter("endDate", endRange);

        return query.getResultList();

    }

}