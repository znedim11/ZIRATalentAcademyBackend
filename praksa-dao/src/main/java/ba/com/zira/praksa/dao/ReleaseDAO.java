package ba.com.zira.praksa.dao;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.dao.model.ReleaseEntity;

@Repository
public class ReleaseDAO extends AbstractDAO<ReleaseEntity, Long> {

    public List<ReleaseEntity> getReleasesPerTimetable(String startRange, String endRange) {

        TypedQuery<ReleaseEntity> query = entityManager.createQuery(
                "SELECT r FROM ReleaseEntity r WHERE releaseDate >= :startDate AND releaseDate <= :endDate", ReleaseEntity.class);

        query.setParameter("startDate", Timestamp.valueOf(startRange));
        query.setParameter("endDate", Timestamp.valueOf(endRange));

        return query.getResultList();

    }

}