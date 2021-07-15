package ba.com.zira.praksa.dao;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ba.com.zira.commons.dao.AbstractDAO;
import ba.com.zira.praksa.api.model.enums.ObjectType;
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

    public List<ReleaseEntity> getReleasesByGamePlatformDevPub(Long gameId, Long platformId, Long devId, Long pubId) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT r FROM ReleaseEntity r WHERE type = :type");

        if (gameId != null) {
            stringBuilder.append(" AND r.game.id = :gameId");
        }
        if (platformId != null) {
            stringBuilder.append(" AND r.platform.id = :platformId");
        }
        if (devId != null) {
            stringBuilder.append(" AND r.developer.id = :devId");
        }

        if (pubId != null) {
            stringBuilder.append(" AND r.publisher.id = :pubId");
        }

        TypedQuery<ReleaseEntity> query = entityManager.createQuery(stringBuilder.toString(), ReleaseEntity.class);

        if (gameId != null) {
            query.setParameter("type", ObjectType.GAME.getValue());
            query.setParameter("gameId", gameId);
        } else {
            query.setParameter("type", ObjectType.PLATFORM.getValue());
        }

        if (platformId != null) {
            query.setParameter("platformId", platformId);
        }

        if (devId != null) {
            query.setParameter("devId", devId);
        }

        if (pubId != null) {
            query.setParameter("pubId", pubId);
        }

        return query.getResultList();
    }

    public List<ReleaseEntity> getReleasesByPlatform(Long platformId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT r FROM ReleaseEntity r WHERE type = :type AND r.platform.id = :platformId");

        TypedQuery<ReleaseEntity> query = entityManager.createQuery(stringBuilder.toString(), ReleaseEntity.class);
        query.setParameter("type", ObjectType.PLATFORM.getValue());
        query.setParameter("platformId", platformId);

        return query.getResultList();
    }

    public List<ReleaseEntity> getReleasesByGame(Long gameId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT r FROM ReleaseEntity r WHERE type = :type AND r.game.id = :gameId");

        TypedQuery<ReleaseEntity> query = entityManager.createQuery(stringBuilder.toString(), ReleaseEntity.class);
        query.setParameter("type", ObjectType.GAME.getValue());
        query.setParameter("gameId", gameId);

        return query.getResultList();
    }

    public List<ReleaseEntity> findByPlatformId(Long id) {
        TypedQuery<ReleaseEntity> query = entityManager.createQuery("SELECT r FROM ReleaseEntity r WHERE r.platform.id = :id",
                ReleaseEntity.class);

        query.setParameter("id", id);

        return query.getResultList();
    }

    public List<ReleaseEntity> findReleases(List<List<Long>> idLists) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT r FROM ReleaseEntity r WHERE type = :type AND r.game.id IN :list1");

        for (int i = 1; i < idLists.size(); i++) {
            stringBuilder.append(String.format(" OR r.game.id IN :list%d", i + 1));
        }

        TypedQuery<ReleaseEntity> query = entityManager.createQuery(stringBuilder.toString(), ReleaseEntity.class);
        query.setParameter("type", ObjectType.GAME.getValue());
        for (int i = 0; i < idLists.size(); i++) {
            query.setParameter(String.format("list%d", i + 1), idLists.get(i));
        }

        return query.getResultList();
    }

}